import useKakaoLoader from '@src/hooks/useKakaoLoader'
import { CustomOverlayMap, Map, Polygon } from 'react-kakao-maps-sdk'
import { useEffect, useRef, useState } from 'react'
import {
  DataBodyType,
  LatLng,
  LatLngDataType,
  PromiseDataType,
  RemakeType,
} from '@src/types/MapType'
import {
  fetchAdministration,
  fetchCommercial,
  fetchDistrict,
} from '@src/api/mapApi'
import { useQuery } from '@tanstack/react-query'
import styled from 'styled-components'
import useSelectPlaceStore from '@src/stores/selectPlaceStore'

// 마우스 호버 시 뜨는 지역 명
const PlaceBox = styled.div`
  position: absolute;
  background: #236eff;
  color: #ffffff;
  //border: 1px solid #888;
  border-radius: 3px;
  font-size: 12px;
  font-weight: 500;
  top: -5px;
  left: 15px;
  padding: 5px;
`

const KakaoMap = () => {
  // 카카오 지도 호출 코드
  useKakaoLoader()

  const mapRef = useRef<kakao.maps.Map>(null)

  // 마우스 올렸을 때 해당 동네 이름 저장
  const [isMouseOver, setIsMouseOver] = useState<string>('')

  // 마우스 올렸을 때 해당 동네 좌표 저장
  const [mouseOverLatLng, setMouseOverLatLng] = useState<LatLng>({
    lat: 37.57023786206844,
    lng: 126.94665085220238,
  })

  // 현재 지도 level 저장한 값
  const [level, setLevel] = useState<number>(8)

  // 지도의 중심 좌표
  const [centerLatLng, setCenterLatLng] = useState<LatLng>({
    lat: 37.57023786206844,
    lng: 126.94665085220238,
  })
  // 현재 화면 우상단, 좌하단 좌표 값
  const [latLngData, setLatLngData] = useState<LatLngDataType>({
    lngNE: 127.22560147553521,
    latNE: 37.668539165787855,
    lngSW: 126.75076041880565,
    latSW: 37.46984057457333,
  })

  // 재가공한 구 데이터 저장
  const [loadData, setLoadData] = useState<RemakeType>([
    {
      name: '',
      center: { lat: 0, lng: 0 },
      code: 0,
      path: [{ lat: 0, lng: 0 }],
    },
  ])

  // 지도 움직일 때 좌표 갱신하는 코드
  const updateMapBounds = (map: kakao.maps.Map) => {
    const bounds = map.getBounds()
    const swLatLng = bounds.getSouthWest()
    const neLatLng = bounds.getNorthEast()
    setLevel(map.getLevel())

    setLatLngData({
      lngNE: neLatLng.getLng(),
      latNE: neLatLng.getLat(),
      lngSW: swLatLng.getLng(),
      latSW: swLatLng.getLat(),
    })
  }

  // 좌표 갱신될 때마다 get 요청 보내는 코드
  const { data, isLoading, refetch } = useQuery<PromiseDataType>({
    queryKey: ['fetchPoligonPath', latLngData],
    queryFn: async () => {
      if (level > 6) {
        return fetchDistrict(latLngData)
      }
      if (level > 4) {
        return fetchAdministration(latLngData)
      }
      return fetchCommercial(latLngData)
    },
  })

  // 호출받은 데이터 재가공
  const parsePolygonData = (dataBody: DataBodyType) => {
    return Object.keys(dataBody.coords)
      .map(key => {
        // codes에서 key에 해당하는 중심 좌표 데이터가 존재하는지 확인
        const names = dataBody.names[key]

        return {
          name: key,
          center: {
            lng: names.center[0],
            lat: names.center[1],
          },
          code: names.code,
          path: dataBody.coords[key].map(([lng, lat]) => ({
            lng,
            lat,
          })),
        }
      })
      .filter(item => item !== null) // 중심 좌표가 없어서 null로 처리된 항목들을 제거
  }

  // store에 저장된 구 데이터와 선택한 구, 동, 상권 값 가져올 store
  const {
    districtData,
    selectedDistrict,
    selectedAdministration,
    selectedCommercial,
    setSelectedDistrict,
    setSelectedAdministration,
    setSelectedCommercial,
    loadSelectedAdministration,
    loadSelectedCommercial,
  } = useSelectPlaceStore()

  // 불러온 데이터 재가공한 값을 loadData에 저장시키는 로직
  useEffect(() => {
    if (data) {
      const newData = parsePolygonData(data.dataBody)
      setLoadData(newData)
    }
  }, [data, selectedDistrict, selectedAdministration, selectedCommercial])

  // 코드길이 5 : 구, 8 : 동, 7 : 상권
  // 행정구의 상태가 변했을 때만 실행되는 useEffect
  useEffect(() => {
    // 코드길이 5인 경우만 처리 (행정구)
    if (String(districtData[0]?.districtCode).length === 5) {
      districtData.forEach(district => {
        // 선택한 행정구를 받아온 데이터와 비교해서 일치하는 값 찾기
        if (district.districtName === selectedDistrict.name && mapRef.current) {
          const mapData = mapRef.current
          // 현재 지도 level 6으로 만들어, 줌인
          mapData.setLevel(6)

          // 중심좌표 LatLng 타입으로 생성
          const moveLatLng = new kakao.maps.LatLng(
            district.districtCenter[1],
            district.districtCenter[0],
          )
          // 현재 중심좌표로 할당
          mapData.setCenter(moveLatLng)
          // 현재 화면 좌상단, 우하단 설정
          const bounds = mapData.getBounds()
          const swLatLng = bounds.getSouthWest()
          const neLatLng = bounds.getNorthEast()
          // setLevel(map.getLevel())

          setLatLngData({
            lngNE: neLatLng.getLng(),
            latNE: neLatLng.getLat(),
            lngSW: swLatLng.getLng(),
            latSW: swLatLng.getLat(),
          })
        }
      })
    }
  }, [districtData, selectedDistrict])

  // TODO 동, 상권 목록 받을 때 중심좌표도 받아오게 되면 함께 띄우도록 수정하겠습니다.

  // 행정동의 상태가 변했을 때만 실행되는 useEffect
  useEffect(() => {
    // 코드길이 5인 경우만 처리 (행정구)
    if (
      String(loadSelectedAdministration[0]?.administrationCode).length === 8
    ) {
      loadSelectedAdministration.forEach(district => {
        // 선택한 행정동을 받아온 데이터와 비교해서 일치하는 값 찾기
        if (
          district.administrationCodeName === selectedAdministration.name &&
          mapRef.current
        ) {
          const mapData = mapRef.current
          // 현재 지도 level 5으로 만들어, 줌인
          mapData.setLevel(5)

          // 중심좌표 LatLng 타입으로 생성
          const moveLatLng = new kakao.maps.LatLng(
            district.centerLat,
            district.centerLng,
          )
          // 현재 중심좌표로 할당
          mapData.setCenter(moveLatLng)
          // 현재 화면 좌상단, 우하단 설정
          const bounds = mapData.getBounds()
          const swLatLng = bounds.getSouthWest()
          const neLatLng = bounds.getNorthEast()
          // console.log(district.centerLat)
          // console.log(district.centerLng)
          setLatLngData({
            lngNE: neLatLng.getLng(),
            latNE: neLatLng.getLat(),
            lngSW: swLatLng.getLng(),
            latSW: swLatLng.getLat(),
          })
        }
      })
    }
  }, [districtData, loadSelectedAdministration, selectedAdministration])

  // 상권의 상태가 변했을 때만 실행되는 useEffect
  useEffect(() => {
    // 코드길이 7인 경우만 처리 (상권)
    if (String(loadSelectedCommercial[0]?.commercialCode).length === 7) {
      loadSelectedCommercial.forEach(district => {
        // 선택한 상권을 받아온 데이터와 비교해서 일치하는 값 찾기
        if (
          district.commercialCodeName === selectedCommercial.name &&
          mapRef.current
        ) {
          const mapData = mapRef.current
          // 현재 지도 level 4으로 만들어, 줌인
          mapData.setLevel(4)
          console.log()
          // 중심좌표 LatLng 타입으로 생성
          const moveLatLng = new kakao.maps.LatLng(
            district.centerLat,
            district.centerLng,
          )
          // console.log(district.centerLat)
          // console.log(district.centerLng)
          // // 현재 중심좌표로 할당
          mapData.setCenter(moveLatLng)
          // 현재 화면 좌상단, 우하단 설정
          const bounds = mapData.getBounds()
          const swLatLng = bounds.getSouthWest()
          const neLatLng = bounds.getNorthEast()
          // setLevel(map.getLevel())

          setLatLngData({
            lngNE: neLatLng.getLng(),
            latNE: neLatLng.getLat(),
            lngSW: swLatLng.getLng(),
            latSW: swLatLng.getLat(),
          })
        }
      })
    }
  }, [
    loadData,
    loadSelectedAdministration,
    loadSelectedCommercial,
    selectedCommercial,
  ])

  useEffect(() => {
    refetch()
  }, [
    loadData,
    selectedDistrict,
    selectedAdministration,
    selectedCommercial,
    refetch,
  ])

  return (
    <div>
      <div>
        <Map
          center={centerLatLng}
          style={{ width: '100%', height: 'calc(100vh - 68px)' }}
          ref={mapRef}
          level={level}
          onDragEnd={map => updateMapBounds(map)}
          onZoomChanged={map => updateMapBounds(map)}
          onClick={map => updateMapBounds(map)}
          minLevel={9}
          // 마우스가 움직일 때 위치를 위경도로 저장
          onMouseMove={(_map, mouseEvent) =>
            setMouseOverLatLng({
              lat: mouseEvent.latLng.getLat(),
              lng: mouseEvent.latLng.getLng(),
            })
          }
        >
          {!isLoading && data
            ? loadData.map((code, index) => {
                // 색상 배열 정의
                const colors = [
                  '#bbe0ff',
                  '#98c6ff',
                  '#7ca4ff',
                  '#b6d4fd',
                  '#679cff',
                  '#8296ff',
                ]
                // index에 따라 색상을 순환시키기 위한 계산
                const colorIndex = index % colors.length
                // 현재 폴리곤의 색상
                const fillColor = colors[colorIndex]

                return (
                  <Polygon
                    key={code.name}
                    path={code.path}
                    strokeWeight={3} // 선의 두께입니다
                    strokeColor={fillColor} // 선의 색깔입니다
                    strokeOpacity={0.5} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                    strokeStyle="longdash" // 선의 스타일입니다
                    // strokeColor="#39DE2A" // 선의 색깔입니다
                    // fillColor={isMouseOver ? '#70ff5c' : '#A2FF99'} // 채우기 색깔입니다
                    fillColor={fillColor} // 채우기 색깔입니다
                    zIndex={100}
                    fillOpacity={isMouseOver === code.name ? 0.8 : 0.2} // 채우기 불투명도입니다
                    onMouseover={() => {
                      // setMouseOverLatLng(code.center)
                      return setIsMouseOver(code.name)
                    }}
                    onMouseout={() => setIsMouseOver('')}
                    onMousedown={() => {}}
                    onClick={() => {
                      if (level > 4) {
                        setLevel(level - 1)
                      }
                      // level이 6보다 크면 행정구 선택하는 화면이 뜹니다.
                      // 행정구 선택 시 선택한 이름, 코드를 저장해서 드롭다운에 띄우는 로직입니다.
                      if (level > 6) {
                        setSelectedDistrict({
                          name: code.name,
                          code: code.code,
                        })
                        // 구 다시 선택하면 동, 상권은 초기화시키는 로직
                        setSelectedAdministration({
                          name: '행정동',
                          code: 0,
                        })
                        setSelectedCommercial({
                          name: '상권',
                          code: 0,
                        })
                        // level 6,5이면 행정동 선택하는 화면
                        // 선택한 행정동 store에 저장해서 드롭다운 갱신
                      } else if (level > 4) {
                        setSelectedAdministration({
                          name: code.name,
                          code: code.code,
                        })
                        // 동의 하위인 상권은 초기화
                        setSelectedCommercial({
                          name: '상권',
                          code: 0,
                        })
                        // 행정동의 8자리 코드 중 5자리 추출
                        const slicedCode = String(code.code).slice(0, 5)
                        // districtData 배열에서 조건에 맞는 항목 찾기
                        const foundDistrict = districtData.find(
                          district =>
                            district.districtCode === Number(slicedCode),
                        )
                        // 행정동 8코드의 앞 5자리는 속한 행정구라서, 행정구 정보 찾아서 store에 저장해서 드롭다운 갱신
                        if (foundDistrict) {
                          setSelectedDistrict({
                            name: foundDistrict.districtName,
                            code: foundDistrict.districtCode,
                          })
                        }
                        // TODO 상권 선택 시 동 정보 받아와 구, 동 갱신시키는 로직 추가예정
                      } else {
                        setSelectedCommercial({
                          name: code.name,
                          code: code.code,
                        })
                      }

                      setCenterLatLng(code.center)
                    }}
                  />
                )
              })
            : ''}
          {isMouseOver && (
            <CustomOverlayMap
              position={{
                // 인포윈도우가 표시될 위치입니다
                lat: mouseOverLatLng.lat,
                lng: mouseOverLatLng.lng,
              }}
            >
              <PlaceBox>{isMouseOver}</PlaceBox>
            </CustomOverlayMap>
          )}
        </Map>
      </div>
    </div>
  )
}

export default KakaoMap
