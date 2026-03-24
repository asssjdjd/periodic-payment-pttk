USE `periodic-payment`

SELECT DISTINCT
    c.*,
    u.*,
    lo.*
FROM contract c
INNER JOIN user u ON c.userId = u.id
INNER JOIN loanoffer lo ON c.loanProductsId = lo.id
WHERE c.customerId = 1
  AND c.status = 'ACTIVE';

  SHOW INDEX FROM contract;

  CREATE INDEX idx_contract_customer_status ON contract(customerId, status);