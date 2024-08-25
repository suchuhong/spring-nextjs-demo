import Axios from 'axios'
// require('dotenv').config()

const httpClient = Axios.create({
  baseURL: process.env.NEXT_PUBLIC_BASE_URL!,
  headers: {
    'X-Requested-With': 'XMLHttpRequest',
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
  withCredentials: true,
  xsrfCookieName: 'XSRF-TOKEN',
  withXSRFToken: true,
})

console.log('Request URL:', httpClient.defaults.baseURL)
console.log('Request Headers:', httpClient.defaults.headers)

export default httpClient
