CREATE TABLE IF NOT EXISTS exchange_rate
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    source_currency VARCHAR (3),
    target_currency VARCHAR (3),
    rate DOUBLE PRECISION,
    PRIMARY KEY (id),
    CONSTRAINT unique_source_target UNIQUE (source_currency, target_currency)
);

-- Create indexes on the source and target columns for faster lookup
CREATE INDEX idx_exchange_rate_source ON exchange_rate(source_currency);
CREATE INDEX idx_exchange_rate_target ON exchange_rate(target_currency);