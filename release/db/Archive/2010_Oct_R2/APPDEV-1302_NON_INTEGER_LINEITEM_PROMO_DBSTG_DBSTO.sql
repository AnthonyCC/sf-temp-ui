-- Change the size of the column to allow more precision for decimal values.
ALTER table cust.orderline MODIFY (promotion_amt number(12,4)); 