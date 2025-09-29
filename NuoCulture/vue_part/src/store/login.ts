
import { defineStore } from 'pinia'

export const useLoginStore = defineStore('login', {
  state: () => ({
    account: '',
    password: ''
  }),
  actions: {
    setCredentials(account: string, password: string) {
      this.account = account
      this.password = password
    },
    clearCredentials() {
      this.account = ''
      this.password = ''
    }
  }
})
