import { useState, useEffect } from 'react';
import type { Contract } from '../types';
import { customerApi } from '../api/client';
import '../styles/ContractList.css';

interface ContractListProps {
  customerId: string;
  onContractSelect: (contract: Contract) => void;
}

export default function ContractList({ customerId, onContractSelect }: ContractListProps) {
  const [contracts, setContracts] = useState<Contract[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const formatDateTime = (value: string) => {
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) return value;
    return date.toLocaleString('vi-VN');
  };

  const getFirstSchedule = (contract: Contract) => contract.paymentSchedules?.[0];

  const formatPercent = (value?: number) =>
    value !== undefined ? `${(value * 100).toFixed(2)}%` : '—';

  useEffect(() => {
    setLoading(true);
    setError('');

    const fetchData = async () => {
      try {
        const response = await customerApi.getActiveContracts(customerId);
        if (response.code === 200) {
          setContracts(response.data);
        } else {
          setError(response.message || 'Không thể tải danh sách hợp đồng');
        }
      } catch (err) {
        setError('Lỗi kết nối tới máy chủ');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [customerId]);

  if (loading) return <div className="loading">Đang tải hợp đồng...</div>;
  if (error) return <div className="error-message">{error}</div>;

  return (
    <div className="contract-list-container">
      <h3>Danh Sách Hợp Đồng Hoạt Động</h3>

      {contracts.length === 0 ? (
        <div className="no-data">Không có hợp đồng hoạt động</div>
      ) : (
        <div className="contracts">
          {contracts.map((contract) => (
            <div key={contract.id} className="contract-card">
              <div className="contract-header">
                <h4>{contract.code}</h4>
                <span className={`status ${contract.status.toLowerCase()}`}>{contract.status}</span>
              </div>

              <div className="contract-details">
                <div className="detail-row">
                  <span className="label">Số tiền vay:</span>
                  <span className="value">{contract.loanAmount.toLocaleString('vi-VN')} VNĐ</span>
                </div>
                <div className="detail-row">
                  <span className="label">Ngày ký hợp đồng:</span>
                  <span className="value">{formatDateTime(contract.signedDate)}</span>
                </div>
                <div className="detail-row">
                  <span className="label">Gói vay:</span>
                  <span className="value">
                    {contract.loanOffer?.name || `${contract.paymentSchedules.length} kỳ trả góp`}
                  </span>
                </div>
                <div className="detail-row">
                  <span className="label">Lãi suất:</span>
                  <span className="value">
                    {formatPercent(
                      contract.loanOffer?.interestRate ?? getFirstSchedule(contract)?.interestDueRate
                    )}
                  </span>
                </div>
                <div className="detail-row">
                  <span className="label">Phí phạt:</span>
                  <span className="value">
                    {contract.loanOffer?.penaltyRate !== undefined
                      ? `${(contract.loanOffer.penaltyRate * 100).toFixed(2)}%`
                      : `${(getFirstSchedule(contract)?.penaltyFee ?? 0).toLocaleString('vi-VN')} VNĐ`}
                  </span>
                </div>
                <div className="detail-row">
                  <span className="label">Lãi suất quá hạn:</span>
                  <span className="value">
                    {formatPercent(
                      contract.loanOffer?.overdueInterestRate ??
                        getFirstSchedule(contract)?.overdueInterestRate
                    )}
                  </span>
                </div>
                
                <div className="detail-row">
                  <span className="label">Kỳ hạn:</span>
                  <span className="value">
                    {contract.loanOffer?.termMonths !== undefined
                      ? `${contract.loanOffer.termMonths} tháng`
                      : `${contract.paymentSchedules.length} kỳ`}
                  </span>
                </div>
                <div className="detail-row">
                  <span className="label">Số kỳ thanh toán còn lại:</span>
                  <span className="value">
                    {contract.paymentSchedules.filter((p) => p.status !== 'PAID').length} /
                    {contract.paymentSchedules.length}
                  </span>
                </div>
              </div>

              <button onClick={() => onContractSelect(contract)} className="select-btn">
                Thanh Toán
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
