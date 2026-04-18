import axios from 'axios';
import type { AxiosInstance, AxiosResponse } from 'axios';
import type {
  ApiResponse,
  Customer,
  Contract,
  LoanPaymentSchedule,
  PaymentRequest,
  OutstandingDebtStatisticsData,
} from '../types';

const USER_SERVICE_URL = 'http://localhost:8082/api/v1';
const PAYMENT_SERVICE_URL = 'http://localhost:8080/api/v1';
const STATISTICS_SERVICE_URL = 'http://localhost:8081/api/v1';

const userServiceInstance: AxiosInstance = axios.create({
  baseURL: USER_SERVICE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const paymentServiceInstance: AxiosInstance = axios.create({
  baseURL: PAYMENT_SERVICE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const statisticsServiceInstance: AxiosInstance = axios.create({
  baseURL: STATISTICS_SERVICE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const normalizeSchedule = (schedule: LoanPaymentSchedule): LoanPaymentSchedule => ({
  ...schedule,
  penaltyDue: schedule.penaltyDue ?? schedule.penaltyFee ?? 0,
  overduePrinciple: schedule.overduePrinciple ?? 0,
  overduePrinciplePaid: schedule.overduePrinciplePaid ?? 0,
});

export const customerApi = {
  searchByName: (name: string): Promise<ApiResponse<Customer[]>> =>
    userServiceInstance
      .get('/customers/search', {
      params: { name },
      })
      .then((res: AxiosResponse<ApiResponse<Customer[]>>) => res.data),

  searchByCccd: (cccd: string): Promise<ApiResponse<Customer[]>> =>
    userServiceInstance
      .get('/customers/search-by-cccd', {
      params: { cccd },
      })
      .then((res: AxiosResponse<ApiResponse<Customer[]>>) => res.data),

  getActiveContracts: (customerId: string): Promise<ApiResponse<Contract[]>> =>
    paymentServiceInstance
      .get(`/payments/${customerId}/contracts/active`)
      .then((res: AxiosResponse<ApiResponse<Contract[]>>) => res.data),

  getPaymentSchedule: (
    customerId: string,
    contractId: string
  ): Promise<ApiResponse<LoanPaymentSchedule[]>> =>
    paymentServiceInstance
      .get(`/payments/${customerId}/schedule/${contractId}`)
      .then((res: AxiosResponse<ApiResponse<LoanPaymentSchedule[]>>) => ({
        ...res.data,
        data: res.data.data.map(normalizeSchedule),
      })),

  executePayment: (
    customerId: string,
    request: PaymentRequest
  ): Promise<ApiResponse<LoanPaymentSchedule>> =>
    paymentServiceInstance
      .post(`/payments/${customerId}/contracts/payment`, request)
      .then((res: AxiosResponse<ApiResponse<LoanPaymentSchedule>>) => ({
        ...res.data,
        data: normalizeSchedule(res.data.data),
      })),
};

export const statisticsApi = {
  getOutstandingDebtDetail: (): Promise<ApiResponse<OutstandingDebtStatisticsData>> =>
    statisticsServiceInstance.post('/statistics/customer/outstanding-debt/detail', {
      fromDate: null,
      endDate: null,
      minDebt: null,
      maxDebt: null
    })
      .then((res: AxiosResponse<ApiResponse<OutstandingDebtStatisticsData>>) => res.data),
};

export default paymentServiceInstance;
