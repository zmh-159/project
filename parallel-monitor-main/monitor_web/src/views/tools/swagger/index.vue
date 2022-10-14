<template>
  <div>
    {{ mycookie }}
    <elFrame :src="swaggerApi" />
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import elFrame from '@/components/Iframe/index'
export default {
  name: 'Swagger',
  components: { elFrame },
  data() {
    return {
      mycookie: null
    }
  },
  computed: {
    ...mapGetters([
      'swaggerApi'
    ])
  },
  created() {
    console.log('hello')
    var x = document.cookie.split(';')
    this.mycookie = unescape(x)
  }
}
</script>
