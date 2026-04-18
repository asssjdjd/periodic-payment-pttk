import { useState } from 'react';
import type { Customer, Contract } from './types';
import SearchCustomer from './components/SearchCustomer';
import ContractList from './components/ContractList';
import PaymentSchedule from './components/PaymentSchedule';
import SupplierManagement from './components/SupplierManagement';
import './App.css';


import CustomerDebtStatistics from './components/CustomerDebtStatistics';

function App() {
  const [selectedCustomer, setSelectedCustomer] = useState<Customer | null>(null);
  const [selectedContract, setSelectedContract] = useState<Contract | null>(null);
  const [paymentCompleted, setPaymentCompleted] = useState(false);
  const [showDebtStatistics, setShowDebtStatistics] = useState(false);
  const [showSupplierManagement, setShowSupplierManagement] = useState(false);
  const [showContractPaymentSearch, setShowContractPaymentSearch] = useState(false);

  const handleCustomerSelect = (customer: Customer) => {
    setSelectedCustomer(customer);
    setSelectedContract(null);
    setShowDebtStatistics(false);
    setShowSupplierManagement(false);
    setShowContractPaymentSearch(true);
  };

  const handleContractSelect = (contract: Contract) => {
    setSelectedContract(contract);
  };

  const handlePaymentComplete = () => {
    setPaymentCompleted(!paymentCompleted);
  };

  const handleBack = () => {
    if (selectedContract) {
      setSelectedContract(null);
    } else if (selectedCustomer) {
      setSelectedCustomer(null);
    } else if (showDebtStatistics) {
      setShowDebtStatistics(false);
    } else if (showSupplierManagement) {
      setShowSupplierManagement(false);
    } else if (showContractPaymentSearch) {
      setShowContractPaymentSearch(false);
    }
  };

  return (
    <div className="app-container">
      <header className="app-header">
        <h1>Hệ Thống Thanh Toán Khoản Vay Theo Kỳ Hạn</h1>
        <p>Tìm kiếm khách hàng, xem hợp đồng và thực hiện thanh toán</p>
      </header>

      <main className="app-main">
        {!selectedCustomer && !showDebtStatistics && !showSupplierManagement ? (
          <>
            <div className="home-action-buttons">
              <button
                className="home-action-btn payment"
                onClick={() => {
                  setShowContractPaymentSearch(true);
                  setShowDebtStatistics(false);
                  setShowSupplierManagement(false);
                }}
              >
                Thanh Toán Hợp Đồng
              </button>
              <button
                className="home-action-btn"
                onClick={() => {
                  setShowDebtStatistics(true);
                  setShowSupplierManagement(false);
                  setShowContractPaymentSearch(false);
                }}
              >
                Xem Thống Kê Dư Nợ
              </button>
              <button
                className="home-action-btn supplier"
                onClick={() => {
                  setShowSupplierManagement(true);
                  setShowDebtStatistics(false);
                  setShowContractPaymentSearch(false);
                  setSelectedCustomer(null);
                  setSelectedContract(null);
                }}
              >
                Quản Lý Nhà Cung Cấp
              </button>
            </div>
            {showContractPaymentSearch && <SearchCustomer onCustomerSelect={handleCustomerSelect} />}
          </>
        ) : showSupplierManagement ? (
          <div className="view-container">
            <div className="view-header">
              <h2>Quản Lý Nhà Cung Cấp</h2>
              <button onClick={handleBack} className="back-btn">
                ← Quay Lại
              </button>
            </div>
            <SupplierManagement />
          </div>
        ) : showDebtStatistics ? (
          <div className="view-container">
            <div className="view-header">
              <h2>Thống Kê Dư Nợ Khách Hàng</h2>
              <button onClick={handleBack} className="back-btn">
                ← Quay Lại
              </button>
            </div>
            <CustomerDebtStatistics />
          </div>
        ) : !selectedContract ? (
          <div className="view-container">
            <div className="view-header">
              <h2>
                Khách Hàng:
                <span className="customer-name">{selectedCustomer ? selectedCustomer.fullName : ''}</span>
              </h2>
              <button onClick={handleBack} className="back-btn">
                ← Quay Lại
              </button>
            </div>
            {selectedCustomer && (
              <ContractList
                customerId={selectedCustomer.id}
                onContractSelect={handleContractSelect}
              />
            )}
          </div>
        ) : (
          <div className="view-container">
            <div className="view-header">
              <h2>
                Hợp Đồng:
                <span className="contract-code">{selectedContract.code}</span>
              </h2>
              <button onClick={handleBack} className="back-btn">
                ← Quay Lại
              </button>
            </div>
            {selectedCustomer && selectedContract && (
              <PaymentSchedule
                customerId={selectedCustomer.id}
                contractId={selectedContract.id}
                onPaymentComplete={handlePaymentComplete}
              />
            )}
          </div>
        )}
      </main>

      <footer className="app-footer">
        <p>&copy; 2026 Hệ Thống Thanh Toán Vay Theo Kỳ Hạn | Phiên Bản 1.0</p>
      </footer>
    </div>
  );
}

export default App;
