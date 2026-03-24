CREATE INDEX idx_contract_customer_status ON contract(customerId, status);

SHOW INDEX FROM contract;