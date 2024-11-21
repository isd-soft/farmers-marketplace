import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import PrimeVue from 'primevue/config'
import Aura from '@primevue/themes/aura'
import Button from 'primevue/button'
import SelectButton from 'primevue/selectbutton';
import InputText from 'primevue/inputtext'
import FloatLabel from 'primevue/floatlabel';


const app = createApp(App)

app.use(PrimeVue, {
  theme: {
      preset: Aura,
      options: {
        prefix: 'p',
        darkModeSelector: 'false',
      }
  }
})
app.use(router)

app.component('ThemedButton', Button)
app.component('SelectButton', SelectButton)
app.component('InputText', InputText)
app.component('FloatLabel', FloatLabel)

app.mount('#app')
