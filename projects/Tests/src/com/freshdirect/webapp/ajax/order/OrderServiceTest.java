package com.freshdirect.webapp.ajax.order;

import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.mockito.Mockito;

import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.util.JspMethods;

import junit.framework.Assert;

public class OrderServiceTest {

    private FDUserI user = Mockito.mock(FDUserI.class);
    private FDOrderI order = Mockito.mock(FDOrderI.class);
    private HttpSession session = Mockito.mock(HttpSession.class);

    @Test
    public void validateAcceptOnlyNumberInOrderId() {
        Assert.assertTrue(OrderService.defaultService().isOrderValid("123465789"));
    }

    @Test
    public void validateNotAcceptEmptyOrderId() {
        Assert.assertFalse(OrderService.defaultService().isOrderValid(""));
    }

    @Test
    public void validateNotAcceptLetterInOrderId() {
        Assert.assertFalse(OrderService.defaultService().isOrderValid("wrong13246579"));
    }

    @Test
    public void validateNotAcceptWhitespaceInOrderId() {
        Assert.assertFalse(OrderService.defaultService().isOrderValid(" "));
    }

    @Test
    public void validateNotAcceptNegativeNumberInOrderId() {
        Assert.assertFalse(OrderService.defaultService().isOrderValid("-1323"));
    }

    @Test
    public void populateEmptyOrder() {
        OrderData orderData = OrderService.defaultService().populateOrderData(session, user, order);
        Assert.assertNotNull(orderData);
        Assert.assertNull(orderData.getOrderId());
        Assert.assertEquals(JspMethods.formatPrice(0), orderData.getEstimatedTotal());
        Assert.assertEquals(JspMethods.formatPrice(0), orderData.getSubTotal());
        Assert.assertEquals(JspMethods.formatPrice(0), orderData.getTotalWithoutTax());
        Assert.assertFalse(orderData.isContainsWineSection());
        Assert.assertEquals(0d, orderData.getItemCount());
    }

    @Test
    public void populateTotalBoxOfOrder() {
        String orderId = "132546798";
        double total = 100d;
        double subTotal = 80d;
        double tax = 5d;

        Mockito.when(order.getErpSalesId()).thenReturn(orderId);
        Mockito.when(order.getTotal()).thenReturn(total);
        Mockito.when(order.getSubTotal()).thenReturn(subTotal);
        Mockito.when(order.getTaxValue()).thenReturn(tax);

        OrderData orderData = OrderService.defaultService().populateOrderData(session, user, order);

        Assert.assertEquals(orderId, orderData.getOrderId());
        Assert.assertEquals(JspMethods.formatPrice(total), orderData.getEstimatedTotal());
        Assert.assertEquals(JspMethods.formatPrice(subTotal), orderData.getSubTotal());
        Assert.assertEquals(JspMethods.formatPrice(total - tax), orderData.getTotalWithoutTax());
    }

}
