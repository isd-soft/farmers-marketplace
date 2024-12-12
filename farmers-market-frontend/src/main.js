import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import PrimeVue from 'primevue/config'
import Aura from '@primevue/themes/aura'
import Button from 'primevue/button'
import SelectButton from 'primevue/selectbutton';
import InputText from 'primevue/inputtext'
import FloatLabel from 'primevue/floatlabel';
import ToastService from 'primevue/toastservice'
import Toast from 'primevue/toast'
import ConfirmationService from 'primevue/confirmationservice';
import AOS from 'aos'
import 'aos/dist/aos.css'


import 'primeicons/primeicons.css';
import './assets/main.css'
import './assets/overrides.css';


const app = createApp(App)

AOS.init()

app.use(PrimeVue, {
  theme: {
      preset: Aura,
      options: {
        prefix: 'p',
        darkModeSelector: 'false',
      }
  }
})

app.use(ToastService)
app.use(ConfirmationService);
app.use(router)

app.component('ThemedButton', Button)
app.component('SelectButton', SelectButton)
app.component('InputText', InputText)
app.component('FloatLabel', FloatLabel)
app.component('Toast', Toast)

app.config.globalProperties.$toast = app.config.globalProperties.$toast || null

app.mount('#app')

