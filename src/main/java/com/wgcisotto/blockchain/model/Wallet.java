package com.wgcisotto.blockchain.model;

import com.wgcisotto.blockchain.utils.CryptographyHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
public class Wallet {

    private PrivateKey privateKey;

    private PublicKey publicKey;

    public Wallet(){
        KeyPair keyPair = CryptographyHelper.ellipticCurveCrypto();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public double balance() {
        return ownedUxto()
                .stream()
                .mapToDouble(TransactionOutput::getAmount)
                .sum();
    }

    private List<TransactionOutput> ownedUxto() {
        return BlockChain.UTXOs.values()
                .stream()
                .filter(transactionOutput -> transactionOutput.isMine(publicKey)).collect(Collectors.toList());
    }

    public Optional<Transaction> transfer(PublicKey receiver, double amount){

        if(balance() < amount){
            log.error("Not enough balance");
            return Optional.empty();
        }

        List<TransactionInput> inputs = new ArrayList<>();

        while(inputs.stream().mapToDouble(transaction -> transaction.getUTXO().getAmount()).sum() < amount) {
            TransactionOutput transactionOutput = ownedUxto().get(0);
            inputs.add(TransactionInput.builder()
                            .transactionOutputId(transactionOutput.getId())
                            .UTXO(transactionOutput)
                    .build());
           // BlockChain.UTXOs.remove(transactionOutput.getId());
        }

        Transaction transaction = new Transaction(publicKey, receiver, amount, inputs);
        transaction.sign(privateKey);

        return Optional.of(transaction);
    }

}
