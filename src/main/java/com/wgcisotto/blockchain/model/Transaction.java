package com.wgcisotto.blockchain.model;

import com.wgcisotto.blockchain.utils.CryptographyHelper;
import com.wgcisotto.blockchain.utils.SHA256Helper;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class Transaction {

    private String transactionId;
    
    private PublicKey sender;
    
    private PublicKey receiver;
    
    public List<TransactionInput> inputs;
    
    public List<TransactionOutput> outputs;
    
    private double amount;
    
    private  byte[] signature;
    
    public Transaction(PublicKey sender, PublicKey receiver, double amount, List<TransactionInput> inputs){
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.inputs = inputs;
        this.outputs = new ArrayList<>();
        calculateHash();
    }

    public boolean verifyTransaction(){

        if(!verifySignature()){
            log.error("Invalid transaction! Signature invalid.");
            return false;
        }

        inputs.forEach(transactionInput -> {
            transactionInput.setUTXO(BlockChain.UTXOs.get(transactionInput.getTransactionOutputId()));
        });

        outputs.add(new TransactionOutput(receiver, amount, transactionId));
        outputs.add(new TransactionOutput(sender, totalTransactionAmount() - amount, transactionId));
        outputs.forEach(transactionOutput -> BlockChain.UTXOs.put(transactionOutput.getId(), transactionOutput));
        inputs.forEach(transactionInput -> BlockChain.UTXOs.remove(transactionInput.getTransactionOutputId()));


        return true;
    }

    public void sign(PrivateKey privateKey){
        String dataToSign = sender.toString() + receiver.toString() + amount;
        this.signature = CryptographyHelper.applyECDSASignature(privateKey, dataToSign);
    }

    private boolean verifySignature(){
        String data = sender.toString() + receiver.toString() + amount;
        return CryptographyHelper.verifyECDSASignature(sender, data, signature);
    }

    private void calculateHash() {
        this.transactionId = SHA256Helper.hash(sender.toString() + receiver.toString() + amount);
    }

    private double totalTransactionAmount(){
        return inputs.stream()
                .mapToDouble(transactionInput -> transactionInput.getUTXO().getAmount())
                .sum();
    }

}
