import {
  BarElement,
  CategoryScale,
  Chart as ChartJS,
  Legend,
  LinearScale,
  Plugin,
  Title,
  Tooltip,
} from 'chart.js'
import { Bar } from 'react-chartjs-2'

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)

interface BarChartProps {
  labels: string[]
  values: number[]
  datasetsLabel: string
  minvalue: number
  pluginUnit: string
}

const BarChartCompare2 = (props: BarChartProps) => {
  const { labels, values, datasetsLabel, minvalue, pluginUnit } = props

  const data = {
    labels,
    datasets: [
      {
        label: datasetsLabel,
        data: values,
        backgroundColor: [
          'rgba(4, 191, 218, 0.4)',
          'rgba(255, 168, 74, 0.4)',
          'rgba(255, 168, 74, 0.4)',
        ],
        borderColor: [
          'rgba(4, 191, 218, 1)',
          'rgba(255, 168, 74, 1)',
          'rgba(255, 168, 74, 1)',
        ],
        borderWidth: 1,
      },
    ],
  }

  const options = {
    responsive: true,
    interaction: {
      intersect: false,
    },
    layout: {
      padding: {
        top: 20,
        left: 20,
        right: 20,
      },
    },
    plugins: {
      legend: {
        display: false,
      },
    },
    scales: {
      x: {
        grid: {
          display: false,
        },
      },
      y: {
        display: false,
        type: 'logarithmic',
        position: 'left',
        min: 1,
        // 로그 스케일에서는 ticks 설정을 조정하여 레이블을 보기 좋게 할 수 있습니다.
        ticks: {
          callback(value: number) {
            return Number(value.toString()) // 이 부분은 데이터에 맞게 조정이 필요합니다.
          },
          stepSize: 1,
        },
        grid: {
          display: false,
        },
      },
    },
    categoryPercentage: 0.5,
  }

  const plugins: Plugin<'bar', unknown>[] = [
    {
      id: 'customCenterValue',
      afterDraw: (chart: ChartJS<'bar', number[], unknown>) => {
        const { ctx } = chart
        ctx.save()
        chart.getDatasetMeta(0).data.forEach((datapoint, index) => {
          ctx.font = 'bolder 14px sans-serif'
          ctx.fillStyle = data.datasets[0].borderColor[index]
          ctx.textAlign = 'center'
          ctx.fillText(
            `${values[index].toLocaleString()}${pluginUnit}`,
            datapoint.x,
            datapoint.y - 10,
          )
        })
      },
    },
  ]

  return (
    <Bar
      key={JSON.stringify(data)}
      options={options}
      data={data}
      plugins={plugins}
    />
  )
}

export default BarChartCompare2
