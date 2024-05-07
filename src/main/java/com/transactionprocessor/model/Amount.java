package com.transactionprocessor.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Amount {

    @NotBlank
    private String amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String debitOrCredit;

//    public Amount(String amount, String currency, String debitOrCredit) throws BadRequestException{
//        try {
//            BigDecimal numberValidation = new BigDecimal(amount);
//        } catch(NumberFormatException e){
//            throw new BadRequestException("Invalid amount entry!", e);
//        }
//        if(!debitOrCredit.equals(DebitCredit.DEBIT.toString()) && !debitOrCredit.equals(DebitCredit.CREDIT.toString())) {
//            throw new BadRequestException("Invalid debitOrCreditValue!");
//        }
//        this.currency = currency;
//        this.debitOrCredit = debitOrCredit;
//    }
//
//    public void setAmount(String amount) throws BadRequestException {
//        try {
//            BigDecimal numberValidation = new BigDecimal(amount);
//            this.amount = amount;
//        } catch (NumberFormatException e) {
//            throw new BadRequestException("Invalid amount entry!", e);
//        }
//    }
//
//    public void setDebitOrCredit(String debitOrCredit) throws BadRequestException {
//        try {
//            if(!debitOrCredit.equals(DebitCredit.DEBIT.toString()) && !debitOrCredit.equals(DebitCredit.CREDIT.toString())) {
//                throw new BadRequestException("Invalid debitOrCreditValue!");
//            }
//            this.debitOrCredit = debitOrCredit;
//        } catch (Exception e) {
//            throw e;
//        }
//    }
}
