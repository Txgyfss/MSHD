<template>
  <div>
    <div class="main-body-header">欢迎进入多源灾情数据管理服务系统</div>
    <div class="main-body-content">
      <div style="margin-bottom: 100px; width: 100%">
        <el-collapse v-model="activeName" accordion>
          <el-collapse-item v-for="item in notices" :title="item.name" :name="item.id">
            <div style="padding: 0 20px">{{ item.content }}</div>
          </el-collapse-item>
        </el-collapse>
      </div>
      <div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div id="pieChart" style="height: 400px"></div>
          </el-col>
          <el-col :span="12">
            <div id="mapChart" style="height: 400px"></div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div id="rankingChart" style="height: 400px"></div>
          </el-col>
          <el-col :span="12">
            <div id="barChart" style="height: 400px"></div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <div id="lineChart" style="height: 400px"></div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <div id="stackedBarChart" style="height: 400px"></div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <div id="radarChart" style="height: 400px"></div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <div id="dataFormatChart" style="height: 400px"></div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import * as echarts from 'echarts'; // 引入 ECharts 主模块
import 'echarts/map/js/china'; // 引入中国地图数据

export default {
  name: 'IndexView',
  data() {
    return {
      activeName: '',
      notices: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  mounted() {
    request.get("/notice").then(res => {
      if (res.code === '0') {
        this.notices = res.data;
      } else {
        this.$message.error(res.msg)
      }
    });
    this.initCharts();
  },
  methods: {
    initCharts() {
      this.initPieChart();
      this.initMapChart();
      this.initRankingChart();
      this.initBarChart();
      this.initLineChart();
      this.initStackedBarChart();
      this.initRadarChart();
      this.initDataFormatChart();
    },
    initPieChart() {
      var pieChart = echarts.init(document.getElementById('pieChart'));
      pieChart.setOption({
        // 饼图配置
        title: {
          text: '不同灾情大类信息占比',
          left: 'center',
          textStyle: { color: '#003366', fontSize: 16 }
        },
        tooltip: { trigger: 'item' },
        legend: {
          bottom: '3%',
          left: 'center',
          textStyle: { color: '#003366', fontSize: 10 }
        },
        series: [
          {
            name: '灾情类型',
            type: 'pie',
            radius: ['30%', '50%'],
            label: {
              formatter: '{b}\n{d}%',
              color: '#003366',
              fontSize: 8
            },
            data: [
              { value: 556, name: '业务报送数据' },
              { value: 100, name: '泛在感知数据' },
              { value: 30, name: '其他数据' }
            ],
            color: ['#5470c6', '#91cc75', '#fac858']
          }
        ]
      });
    },
    initMapChart() {
      var mapChart = echarts.init(document.getElementById('mapChart'));
      mapChart.setOption({
        // 地图配置
        title: { text: '灾情分布情况', left: 'center', textStyle: { color: '#003366', fontSize: 16 } },
        tooltip: { trigger: 'item' },
        visualMap: { min: 0, max: 1, left: 'left', textStyle: { color: '#003366' } },
        geo: {
          map: 'china',
          label: { emphasis: { show: false } },
          itemStyle: { normal: { areaColor: '#d9f7be', borderColor: '#69c0ff' } }
        },
        series: [
          {
            name: '灾情',
            type: 'scatter',
            coordinateSystem: 'geo',
            data: [
              { name: '四川', value: [104.07, 30.67, 0.888] },
              { name: '云南', value: [102.72, 25.04, 0.777] },
              { name: '安徽', value: [117.27, 31.86, 0.555] }
            ],
            itemStyle: { color: '#ff7875' }
          }
        ]
      });
    },
    initRankingChart() {
      var rankingChart = echarts.init(document.getElementById('rankingChart'));
      rankingChart.setOption({
        // 排名图配置
        title: { text: '按城市灾情数量排名', left: 'center', textStyle: { color: '#003366' } },
        tooltip: { trigger: 'axis' },
        yAxis: {
          type: 'category',
          data: ['宁波', '台州', '上海', '长沙', '武汉'],
          axisLabel: { color: '#003366', fontSize: 8 }
        },
        xAxis: { type: 'value', axisLabel: { color: '#003366', fontSize: 8 } },
        series: [
          {
            type: 'bar',
            data: [180, 250, 300, 150, 200],
            itemStyle: { color: '#40a9ff', fontSize: 8 }
          }
        ]
      });
    },
    initBarChart() {
      var barChart = echarts.init(document.getElementById('barChart'));
      barChart.setOption({
        // 柱状图配置
        title: { text: '灾情类型分布', left: 'center', textStyle: { color: '#003366' } },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: ['震情', '人员伤亡', '次生灾害', '诱发灾害', '经济损失'], axisLabel: { color: '#003366', fontSize: 7 } },
        yAxis: { type: 'value', axisLabel: { color: '#003366', fontSize: 8 } },
        series: [
          {
            type: 'bar',
            data: [80, 60, 40, 30, 20],
            itemStyle: { color: '#91cc75' }
          }
        ]
      });
    },
    initLineChart() {
      var lineChart = echarts.init(document.getElementById('lineChart'));
      lineChart.setOption({
        // 折线图配置
        title: { text: '灾情时段分布', left: 'center', textStyle: { color: '#003366' } },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: ['2024.01', '2024.02', '2024.03', '2024.04', '2024.05'], axisLabel: { color: '#003366' } },
        yAxis: { type: 'value', axisLabel: { color: '#003366' } },
        series: [
          {
            type: 'line',
            data: [80, 180, 120, 20, 250],
            itemStyle: { color: '#5470c6' }
          }
        ]
      });
    },
    initStackedBarChart() {
      var stackedBarChart = echarts.init(document.getElementById('stackedBarChart'));
      stackedBarChart.setOption({
        // 堆叠柱状图配置
        title: { text: '灾情数量统计', left: 'center', textStyle: { color: '#003366' } },
        tooltip: { trigger: 'axis' },
        yAxis: {
          type: 'category',
          data: ['2024.01', '2024.02', '2024.03', '2024.04', '2024.05'],
          axisLabel: { color: '#003366', fontSize: 7 }
        },
        xAxis: { type: 'value', axisLabel: { color: '#003366' } },
        series: [
          { name: '震情', type: 'bar', stack: '总量', data: [20, 30, 40, 50, 60], itemStyle: { color: '#91cc75' } },
          { name: '人员伤亡', type: 'bar', stack: '总量', data: [15, 25, 35, 45, 55], itemStyle: { color: '#fac858' } },
          { name: '经济损失', type: 'bar', stack: '总量', data: [10, 20, 30, 40, 50], itemStyle: { color: '#73c0de' } },
          { name: '次生灾害', type: 'bar', stack: '总量', data: [10, 20, 30, 40, 50], itemStyle: { color: '#66af9f' } },
          { name: '诱发灾害', type: 'bar', stack: '总量', data: [10, 20, 30, 40, 50], itemStyle: { color: '#bd6477' } }]
      });
    },
    initRadarChart() {
      var radarChart = echarts.init(document.getElementById('radarChart'));
      radarChart.setOption({
        // 雷达图配置
        title: { text: '灾情类型雷达分布', left: 'center', textStyle: { color: '#003366' } },
        radar: {
          indicator: [
            { name: '震情', max: 100 },
            { name: '人员伤亡', max: 100 },
            { name: '次生灾害', max: 100 },
            { name: '诱发灾害', max: 100 },
            { name: '经济损失', max: 100 }
          ],
          splitArea: { areaStyle: { color: 'rgba(255, 255, 255, 0.1)' } }
        },
        series: [
          { name: '2023年', type: 'radar', data: [{ value: [60, 50, 40, 30, 20], name: '2023年' }] },
          { name: '2024年', type: 'radar', data: [{ value: [70, 60, 50, 40, 30], name: '2024年' }] }
        ]
      });
    },
    initDataFormatChart() {
      var dataFormatChart = echarts.init(document.getElementById('dataFormatChart'));
      dataFormatChart.setOption({
        // 数据格式统计图配置
        title: {
          text: '数据格式统计', // 设置标题
          left: 'center', // 标题居中
          textStyle: { color: '#003366', fontSize: 16 }, // 标题样式
        },
        tooltip: {
          trigger: 'item', // 鼠标悬浮触发提示
          formatter: '{b}: {c}%', // 格式化提示内容
        },
        series: [
          {
            type: 'treemap', // 使用矩形树图
            data: [
              { name: '文字', value: 31, itemStyle: { color: '#73c0de' } }, // 文字数据
              { name: '图像', value: 25, itemStyle: { color: '#ab85c9' } }, // 图像数据
              { name: '音频', value: 17, itemStyle: { color: '#5b9bd5' } }, // 音频数据
              { name: '视频', value: 15, itemStyle: { color: '#9c7248' } }, // 视频数据
              { name: '其他', value: 10, itemStyle: { color: '#7b61ff' } }, // 其他数据
            ],
            label: {
              show: true, // 显示标签
              formatter: '{b}\n{c}%', // 标签格式，显示名称和百分比
              color: '#ffffff', // 标签文字颜色
              fontSize: 12, // 标签字体大小
            },
            breadcrumb: { show: false }, // 禁用导航
            roam: false, // 禁止缩放
          },
        ],
      });
    },
  }
}
</script>