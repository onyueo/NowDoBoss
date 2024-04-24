from fastapi import FastAPI
from pydantic import BaseModel
from sklearn.cluster import kmeans
import joblib
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np
from sklearn.metrics import pairwise_distances
from typing import List

# 유사 행정동 군집
foot_traffic_first = ['11530510', '11440720', '11650621', '11740660', '11215870', '11590510', '11260575', '11710632', '11380552', '11710562', '11215810', '11710580', '11320690', '11620625', '11305615', '11230600', '11215840', '11410620', '11440655', '11215770']
foot_traffic_second = ['11170660', '11200620', '11380632', '11350624', '11530595', '11305575', '11470680', '11710590', '11560670', '11320522', '11140665', '11200645', '11290780', '11290580', '11290590', '11200580', '11305603', '11650620', '11140645', '11620545']
foot_traffic_third = ['11305545', '11230545', '11740610', '11305555', '11380625', '11350595', '11620595', '11650530', '11560535', '11680650', '11290705', '11230560', '11410720', '11470650', '11305534', '11305635', '11680531', '11230536', '11440565', '11410585']

# 유동인구 모델을 불러오고 추천을 수행하는 함수
def get_foot_traffic_recommendations(user_id: int, type: int, code: str, commercial_list: List):
    # 모델 불러오기
    kmeans = joblib.load("models/kmeans_foot_traffic.pkl")

    # 주어진 새로운 데이터 - 받은 상권 코드 리스트들을 가지고 해당 상권 코드에 해당하는 유동인구 관련 데이터 리스트 가져오기
    # 이건 샘플
    data_values = [
        [3457038, 48.455614, 28.487740, 23.056588, 43.210399, -6.824034],
        [3866927, 58.545920, 28.324662, 13.129392, 45.081807, -4.413311],
        [940763, 55.902390, 25.147460, 18.950256, 45.104160, 1.674210],
        [1131589, 44.036837, 27.068043, 28.895120, 45.539604, -2.924892],
        [758392, 41.834038, 28.739755, 29.426339, 49.088934, -10.857430],
        [8433702, 42.462456, 29.820107, 27.717448, 46.550838, -2.046162],
        [6528442, 43.211841, 29.216037, 27.572153, 46.586358, -0.738406],
        [16988146, 43.768331, 29.215578, 27.016085, 47.498907, 0.274199],
        [8350, 51.137725, 28.215569, 20.646707, 45.173653, 5.044660],
        [7175487, 44.224664, 28.404525, 27.370811, 45.820607, -0.767818]
    ]

    new_data = np.array(data_values)

    # 1. 새로운 데이터에 대한 예측된 군집 번호 가져오기
    predicted_clusters = kmeans.predict(new_data)

    # 2. 군집의 중심값 가져오기
    cluster_centers = kmeans.cluster_centers_

    # 3. 각 군집에서의 상위 3개의 유사한 데이터와 유사도 찾기
    top3_foot_traffic_recommendations = [[] for _ in range(len(kmeans.cluster_centers_))]
    for cluster_index in range(len(kmeans.cluster_centers_)):
        cluster_center = cluster_centers[cluster_index]
        similarities_to_cluster_center = cosine_similarity(new_data, [cluster_center])
        cluster_data_indices = np.argsort(similarities_to_cluster_center[:, 0])[::-1][:3]  # 해당 군집의 상위 3개 데이터의 인덱스
        for similarity, data_index in zip(similarities_to_cluster_center[cluster_data_indices], cluster_data_indices):
            similar_data = data_values[data_index]
            top3_foot_traffic_recommendations[cluster_index].append({
                'commercial_code': data_index,
                'similaritiy': similarity[0],
                'data': similar_data[0:],
                'cluster_num': predicted_clusters[data_index]  # 실제 예측된 군집 번호 추가
            })

    # 4. 결과 출력
    print("각 군집별 유사도 Top 3:")
    for cluster_index, top3 in enumerate(top3_foot_traffic_recommendations):
        print(f"Cluster {cluster_index}:")
        for idx, similar in enumerate(top3):
            print(f"Top {idx+1}: {similar}")

    return top3_foot_traffic_recommendations

# 매출 모델을 불러오고 추천을 수행하는 함수
def get_sales_recommendations(user_id: int, type: int, code: str, commercial_list: List):
    kmeans = joblib.load("models/kmeans_sales.pkl")

    # 주어진 새로운 데이터 - 받은 상권 코드 리스트들을 가지고 해당 상권 코드에 해당하는 유동인구 관련 데이터 리스트 가져오기
    # 이건 샘플
    data_values = [
        [47.287750, 2.292817, 0.000000, 5.343152, 25.671641, 19.404640, -1],
        [72.106115, 1.415391, 1.614059, 5.612730, 13.357847, 5.893858, -1],
        [70.041560, 0.000000, 0.000000, 1.220358, 24.526993, 4.211089, -1],
        [54.513798, 13.618983, 0.000000, 3.114275, 28.636141, 0.116803, -1],
        [53.726332, 7.110885, 0.000000, 10.009971, 26.708615, 2.444197, -1]
    ]


    new_data = np.array(data_values)

    # 1. 새로운 데이터에 대한 예측된 군집 번호 가져오기
    predicted_clusters = kmeans.predict(new_data)

    # 2. 군집의 중심값 가져오기
    cluster_centers = kmeans.cluster_centers_

    # 3. 각 군집에서의 상위 3개의 유사한 데이터와 유사도 찾기
    top3_sales_recommendations = [[] for _ in range(len(kmeans.cluster_centers_))]
    for cluster_index in range(len(kmeans.cluster_centers_)):
        cluster_center = cluster_centers[cluster_index]
        similarities_to_cluster_center = cosine_similarity(new_data, [cluster_center])
        cluster_data_indices = np.argsort(similarities_to_cluster_center[:, 0])[::-1][:3]  # 해당 군집의 상위 3개 데이터의 인덱스
        for similarity, data_index in zip(similarities_to_cluster_center[cluster_data_indices], cluster_data_indices):
            similar_data = data_values[data_index]
            top3_sales_recommendations[cluster_index].append({
                'commercial_code': data_index,
                'similaritiy': similarity[0],
                'data': similar_data[0:],
                'cluster_num': predicted_clusters[data_index]  # 실제 예측된 군집 번호 추가
            })

    # 4. 결과 출력
    print("각 군집별 유사도 Top 3:")
    for cluster_index, top3 in enumerate(top3_sales_recommendations):
        print(f"Cluster {cluster_index}:")
        for idx, similar in enumerate(top3):
            print(f"Top {idx+1}: {similar}")
    
    return top3_sales_recommendations

app = FastAPI()

class Commercial(BaseModel):
    commercial_code: str
    commercial_code_name: str

# 추천 요청에 사용될 요청 데이터 모델
class RecommendationRequest(BaseModel):
    user_id: int
    type: int
    code: str
    commercial_list: List[Commercial]


# 유동인구 추천 API 엔드포인트
@app.post("/recommendations/foot/traffic")
async def foot_traffic_recommendations(request: RecommendationRequest):
    user_id = request.user_id
    type = request.type
    commercial_list = request.commercial_list

    recommendations = get_foot_traffic_recommendations(user_id, type, commercial_list)
    return {"foot_traffic_recommendations": recommendations}


# 매출 추천 API 엔드포인트
@app.post("/recommendations/sales")
async def sales_recommendations(request: RecommendationRequest):
    user_id = request.user_id
    type = request.type
    commercial_list = request.commercial_list

    recommendations = get_sales_recommendations(user_id, type, commercial_list)
    return {"sales_recommendations": recommendations}


@app.get("/")
async def root():
    return {"message": "Hello World"}


@app.get("/hello/{name}")
async def say_hello(name: str):
    return {"message": f"Hello {name}"}
