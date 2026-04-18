import axios from 'axios';
import type { AxiosInstance, AxiosResponse } from 'axios';
import type {
  ApiResponse,
  Customer,
  Contract,
  LoanPaymentSchedule,
  PaymentRequest,
  OutstandingDebtStatisticsData,
  Supplier,
  CreateSupplierRequest,
  UpdateSupplierRequest,
  ImportOrder,
  SupplierProduct,
} from '../types';

const USER_SERVICE_URL = 'http://localhost:8082/api/v1';
const PAYMENT_SERVICE_URL = 'http://localhost:8080/api/v1';
const STATISTICS_SERVICE_URL = 'http://localhost:8081/api/v1';
const SUPPLIER_SERVICE_URL = 'http://localhost:8083/api/v1';

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

const supplierServiceInstance: AxiosInstance = axios.create({
  baseURL: SUPPLIER_SERVICE_URL,
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

const normalizeSupplier = (supplier: Supplier): Supplier => ({
  ...supplier,
  status: supplier.deletedAt == null ? 'ACTIVE' : 'INACTIVE',
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

export const supplierApi = {
  createSupplier: (request: CreateSupplierRequest): Promise<ApiResponse<Supplier>> =>
    supplierServiceInstance
      .post('/suppliers', request)
      .then((res: AxiosResponse<ApiResponse<Supplier>>) => ({
        ...res.data,
        data: normalizeSupplier(res.data.data),
      })),

  getSuppliers: (): Promise<ApiResponse<Supplier[]>> =>
    supplierServiceInstance
      .get('/suppliers')
      .then((res: AxiosResponse<ApiResponse<Supplier[]>>) => ({
        ...res.data,
        data: res.data.data.map(normalizeSupplier),
      })),

  updateSupplier: (
    supplierId: string,
    request: UpdateSupplierRequest
  ): Promise<ApiResponse<Supplier>> =>
    supplierServiceInstance
      .put(`/suppliers/${supplierId}`, {
        ...request,
        status: request.status || 'ACTIVE',
      })
      .then((res: AxiosResponse<ApiResponse<Supplier>>) => ({
        ...res.data,
        data: normalizeSupplier({
          ...res.data.data,
          status: request.status,
        }),
      })),

  deleteSupplier: (supplierId: string): Promise<ApiResponse<null>> =>
    supplierServiceInstance
      .delete(`/suppliers/${supplierId}`)
      .then((res: AxiosResponse<ApiResponse<null>>) => res.data),

  getSupplierProducts: (supplierId: string): Promise<ApiResponse<SupplierProduct[]>> =>
    supplierServiceInstance
      .get(`/suppliers/${supplierId}/products`)
      .then((res: AxiosResponse<ApiResponse<SupplierProduct[]>>) => res.data),

  getPendingImportOrders: (supplierName: string): Promise<ApiResponse<ImportOrder[]>> =>
    supplierServiceInstance
      .get('/import-orders/pending', {
        params: { name: supplierName },
      })
      .then((res: AxiosResponse<ApiResponse<ImportOrder[]>>) => res.data),

  getCompletedImportOrders: (supplierName: string): Promise<ApiResponse<ImportOrder[]>> =>
    supplierServiceInstance
      .get('/import-orders/completed', {
        params: { name: supplierName },
      })
      .then((res: AxiosResponse<ApiResponse<ImportOrder[]>>) => res.data),
};

export default paymentServiceInstance;
