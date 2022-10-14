<template>
  <div ref="container" />
</template>

<script>
import * as THREE from 'three'
// 操作三维场景
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'
import Stats from 'three/examples/jsm/libs/stats.module.js'

export default {
  data() {
    return {
      scene: new THREE.Scene(),
      // 建立了一个组对象，包含透明盒子和粒子
      group: new THREE.Group(),
      // JavaScript性能监控器
      stats: new Stats(),
      particles: new THREE.BufferGeometry(),
      // 渲染器
      renderer: new THREE.WebGLRenderer({ antialias: true }),
      element: null,
      s: 300, // 三维场景显示范围控制系数，系数越大，显示的范围越大
      // 网状模型
      timer: null,
      // 最大粒子数
      maxParticleCount: 0,
      // 粒子数
      particleCount: 0,
      // 立方体长宽高
      r: 100,
      particlePositions: null,
      particlesData: [],
      pointCloud: null,
      // linesMesh: null,
      camera: null,
      nodeData: {},
      oldNodeData: {},
      pMaterial: new THREE.PointsMaterial({
        color: 0xffffff,
        size: 3.5,
        blending: THREE.AdditiveBlending,
        transparent: true,
        sizeAttenuation: false
      }),
      initFlag: false,
      xspeed: [],
      yspeed: [],
      zspeed: []
    }
  },
  computed: {
    // 获得实时数据
    realTimeData() {
      return JSON.parse(JSON.stringify(this.$store.state.realTimeData))
    },
    // 粒子活动范围限制
    rHalf() {
      return this.r / 2
    }
  },
  watch: {
    realTimeData: {
      handler(newVal, oldVal) {
        this.nodeData = JSON.parse(JSON.stringify(newVal))
        this.oldNodeData = JSON.parse(JSON.stringify(oldVal))
        this.maxParticleCount = Object.keys(this.nodeData).length
        this.particleCount = Object.keys(this.nodeData).length
        if (this.initFlag === false) {
          this.init(this.nodeData)
        }
        this.animate(this.nodeData, this.oldNodeData)
      },
      deep: true
    }
  },
  mounted() {
    // var that = this
    this.element = this.$refs.container
    // this.init()
    window.addEventListener('resize', this.onWindowResize)
  },
  beforeDestroy() {
    clearInterval(this.timer)
    this.timer = null
  },
  methods: {

    sleep(ms) {
      return new Promise(resolve => setTimeout(resolve, ms))
    },
    // 初始化相机，渲染器，鼠标操作三维场景控件，透明盒子，粒子云
    init(nodeData) {
      this.initFlag = true
      // var axisHelper = new THREE.AxisHelper(800)
      // this.scene.add(axisHelper)
      // 相机初始化
      this.camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 4000)
      this.camera.position.set(300, 150, 300)

      // 鼠标操作三维场景
      const controls = new OrbitControls(this.camera, this.element)
      // 视角可以放大的最近和最远距离
      controls.minDistance = 1
      controls.maxDistance = 1000
      this.scene.add(this.group)

      // 透明盒子
      const helper = new THREE.BoxHelper(new THREE.Mesh(new THREE.BoxGeometry(this.r, this.r, this.r)))
      // helper.position.position.set(100, 0, 100)
      helper.material.color.setHex(0x101010)
      helper.material.blending = THREE.AdditiveBlending
      helper.material.transparent = true
      this.group.add(helper)

      this.particlePositions = new Float32Array(this.maxParticleCount * 3)
      // 创建粒子
      var that = this
      var i = 0
      // 初始化粒子云坐标
      Object.keys(nodeData).forEach((key) => {
        that.$set(that.particlePositions, i * 3, nodeData[key]['cpu_busy'] - that.rHalf)
        that.$set(that.particlePositions, i * 3 + 1, nodeData[key]['disk_rate'] - that.rHalf)
        that.$set(that.particlePositions, i * 3 + 2, nodeData[key]['memory_rate'] - that.rHalf)
        // if (nodeData[key]['cpu_busy'] > that.rHalf) {
        //   that.$set(that.particlePositions, i * 3, nodeData[key]['cpu_busy'] - that.rHalf)
        // } else {
        //   that.$set(that.particlePositions, i * 3, that.rHalf - nodeData[key]['cpu_busy'])
        // }
        // if (nodeData[key]['disk_rate'] > that.rHalf) {
        //   that.$set(that.particlePositions, i * 3 + 1, nodeData[key]['disk_rate'] - that.rHalf)
        // } else {
        //   that.$set(that.particlePositions, i * 3 + 1, that.rHalf - nodeData[key]['disk_rate'])
        // }
        // if (nodeData[key]['memory_rate'] > that.rHalf) {
        //   that.$set(that.particlePositions, i * 3 + 2, nodeData[key]['memory_rate'] - that.rHalf)
        // } else {
        //   that.$set(that.particlePositions, i * 3 + 2, that.rHalf - nodeData[key]['memory_rate'])
        // }
        i++
      })
      console.log(nodeData)
      console.log(that.particlePositions)
      this.particles.setDrawRange(0, this.particleCount)
      this.particles.setAttribute('position', new THREE.BufferAttribute(this.particlePositions, 3).setUsage(THREE.DynamicDrawUsage))
      this.pointCloud = new THREE.Points(this.particles, this.pMaterial)
      this.group.add(this.pointCloud)

      // 渲染器初始化
      this.renderer.setPixelRatio(window.devicePixelRatio)
      this.renderer.setSize(window.innerWidth, window.innerHeight)
      this.renderer.outputEncoding = THREE.sRGBEncoding
      this.renderer.setClearColor(0x000000, 1) // 设置背景颜色
      this.element.appendChild(this.renderer.domElement)

      // 开启渲染
      // this.render()
    },
    // 监听窗口大小
    onWindowResize() {
      this.camera.aspect = window.innerWidth / window.innerHeight
      this.camera.updateProjectionMatrix()
      this.renderer.setSize(window.innerWidth, window.innerHeight)
    },
    // 改变粒子坐标
    animate(nodeData, oldData) {
      var that = this
      var i = 0
      Object.keys(nodeData).forEach((key) => {
        var x = nodeData[key]['cpu_busy']
        var y = nodeData[key]['disk_rate']
        var z = nodeData[key]['memory_rate']
        var oldX = oldData[key]['cpu_busy']
        var oldY = oldData[key]['disk_rate']
        var oldZ = oldData[key]['memory_rate']
        var dx = x - oldX
        var dy = y - oldY
        var dz = z - oldZ
        // console.log('--------------')
        // console.log(x)
        // console.log(oldX)
        // // console.log((oldX - that.rHalf) + that.xspeed[i])
        this.$set(that.xspeed, i, dx * 0.001)
        this.$set(that.yspeed, i, dy * 0.001)
        this.$set(that.zspeed, i, dz * 0.001)
        // console.log(that.xspeed[i])
        // console.log((oldX - that.rHalf) + that.xspeed[i])
        // if (oldX > that.rHalf) {
        //   that.$set(that.particlePositions, i * 3, (oldX - that.rHalf) + that.xspeed[i])
        // } else {
        //   that.$set(that.particlePositions, i * 3, (that.rHalf - oldX) + that.xspeed[i])
        // }
        // if (oldY > that.rHalf) {
        //   that.$set(that.particlePositions, i * 3 + 1, (oldY - that.rHalf) + that.yspeed[i])
        // } else {
        //   that.$set(that.particlePositions, i * 3 + 1, (that.rHalf - oldY) + that.yspeed[i])
        // }
        // if (oldZ > that.rHalf) {
        //   that.$set(that.particlePositions, i * 3 + 2, (oldZ - that.rHalf) + that.zspeed[i])
        // } else {
        //   that.$set(that.particlePositions, i * 3 + 2, (that.rHalf - oldZ) + that.zspeed[i])
        // }

        for (var j = 0; j < 1000; ++j) {
          this.$set(that.particlePositions, i * 3, (oldX - that.rHalf) + that.xspeed[i])
          this.$set(that.particlePositions, i * 3 + 1, (oldY - that.rHalf) + that.yspeed[i])
          this.$set(that.particlePositions, i * 3 + 2, (oldZ - that.rHalf) + that.zspeed[i])
          // setTimeout(function() {

          //   // console.log('9')
          // }, 5000)
        }

        i++
      })
      that.pointCloud.geometry.attributes.position.needsUpdate = true
      // 位置改变请求
      // requestAnimationFrame(this.animate)
      this.render()
    },
    // 渲染函数
    render() {
      // const time = Date.now() * 0.001
      // this.group.rotation.y = time * 0.1
      this.renderer.render(this.scene, this.camera)
      // 自动渲染
      requestAnimationFrame(this.render)
    }
  }

}
</script>

<style>
#container {
  position: absolute;
  width: 100%;
  height: 100%;
}
</style>

