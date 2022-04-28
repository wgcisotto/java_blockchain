package com.wgcisotto.blockchain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionInput {

    private String transactionOutputId;

    private TransactionOutput UTXO;

}
