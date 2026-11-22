
package com.example.SmartCart.Order.Service.impl;

import com.example.SmartCart.Order.Entity.Order;
import com.example.SmartCart.Order.Entity.OrderAddressSnapshot;
import com.example.SmartCart.Order.Entity.OrderItem;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class InvoicePdfService {

    public byte[] generateInvoice(Order order) {

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream content =
                         new PDPageContentStream(document, page)) {

                float y = 780;

                writeText(
                        content,
                        "SMARTCART - TAX INVOICE",
                        50,
                        y,
                        20,
                        true
                );

                y -= 35;

                writeText(content,
                        "Order Number: " + order.getOrderNumber(),
                        50, y, 11, false);

                y -= 18;

                writeText(content,
                        "Order Date: " +
                                order.getCreatedAt().format(
                                        DateTimeFormatter.ofPattern(
                                                "dd-MM-yyyy HH:mm"
                                        )
                                ),
                        50, y, 11, false);

                y -= 18;

                writeText(content,
                        "Order Status: " + order.getStatus(),
                        50, y, 11, false);

                y -= 35;

                writeText(content, "Customer Details", 50, y, 13, true);

                y -= 20;

                writeText(content,
                        order.getCustomer().getFirstName() + " " +
                                order.getCustomer().getLastName(),
                        50, y, 11, false);

                y -= 17;

                writeText(content,
                        order.getCustomer().getEmail(),
                        50, y, 11, false);

                y -= 35;

                writeText(content, "Shipping Address", 50, y, 13, true);

                y -= 20;

                OrderAddressSnapshot address =
                        order.getShippingAddressSnapshot();

                if (address != null) {
                    y = writeAddress(content, address, y);
                }

                y -= 20;

                writeText(content, "Order Items", 50, y, 13, true);

                y -= 25;

                writeText(content, "Product", 50, y, 10, true);
                writeText(content, "Qty", 330, y, 10, true);
                writeText(content, "Price", 385, y, 10, true);
                writeText(content, "Total", 470, y, 10, true);

                y -= 15;

                for (OrderItem item : order.getOrderItems()) {

                    if (y < 120) {
                        // This minimal implementation is one-page.
                        // A multi-page version should be used for long orders.
                        break;
                    }

                    writeText(content,
                            safeText(item.getProductName()),
                            50, y, 9, false);

                    writeText(content,
                            String.valueOf(item.getQuantity()),
                            330, y, 9, false);

                    writeText(content,
                            "INR " + item.getPrice(),
                            385, y, 9, false);

                    writeText(content,
                            "INR " + item.getTotalPrice(),
                            470, y, 9, false);

                    y -= 20;
                }

                y -= 15;

                writeText(content,
                        "Subtotal: INR " + order.getSubtotal(),
                        360, y, 10, false);

                y -= 18;

                writeText(content,
                        "GST (5%): INR " + order.getGstAmount(),
                        360, y, 10, false);

                y -= 18;

                writeText(content,
                        "Delivery: INR " + order.getDeliveryCharge(),
                        360, y, 10, false);

                y -= 18;

                writeText(content,
                        "Discount: INR " + order.getDiscount(),
                        360, y, 10, false);

                y -= 25;

                writeText(content,
                        "GRAND TOTAL: INR " + order.getTotalAmount(),
                        360, y, 13, true);

                y -= 45;

                writeText(content,
                        "Thank you for shopping with SmartCart!",
                        50, y, 11, true);
            }

            document.save(output);
            return output.toByteArray();

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to generate order invoice PDF.",
                    exception
            );
        }
    }

    private float writeAddress(
            PDPageContentStream content,
            OrderAddressSnapshot address,
            float y
    ) throws IOException {

        String[] lines = {
                address.getFullName(),
                address.getPhoneNumber(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity() + ", " + address.getState(),
                address.getCountry() + " - " + address.getPostalCode()
        };

        for (String line : lines) {
            if (line != null && !line.isBlank()) {
                writeText(content, safeText(line), 50, y, 10, false);
                y -= 15;
            }
        }

        return y;
    }

    private void writeText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            int fontSize,
            boolean bold
    ) throws IOException {

        PDType1Font font = new PDType1Font(
                bold
                        ? Standard14Fonts.FontName.HELVETICA_BOLD
                        : Standard14Fonts.FontName.HELVETICA
        );

        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(x, y);
        content.showText(safeText(text));
        content.endText();
    }

    private String safeText(String text) {
        if (text == null) {
            return "";
        }

        // Standard PDF fonts do not support every Unicode character.
        return text.replaceAll("[^\\x20-\\x7E]", "?");
    }
}
