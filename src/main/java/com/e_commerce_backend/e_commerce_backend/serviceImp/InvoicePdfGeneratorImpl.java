package com.e_commerce_backend.e_commerce_backend.serviceImp;

import com.e_commerce_backend.e_commerce_backend.entity.Invoice;
import com.e_commerce_backend.e_commerce_backend.entity.OrderItem;
import com.e_commerce_backend.e_commerce_backend.entity.Orders;
import com.e_commerce_backend.e_commerce_backend.services.InvoicePdfGenerator;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;

import org.springframework.stereotype.Service;


import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Service
public class InvoicePdfGeneratorImpl implements InvoicePdfGenerator {

    @Override
    public byte[] generateProformaInvoice(Orders order) {
        return buildPdf(order, null, "PROFORMA INVOICE", true);
    }

    @Override
    public byte[] generateFinalInvoice(Orders order, Invoice invoice) {
        return buildPdf(order, invoice.getInvoiceNumber(), "TAX INVOICE", false);
    }

    private byte[] buildPdf(Orders order,
                            String invoiceNumber,
                            String title,
                            boolean isUnpaid) {

        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, out);

            document.open();

            /* ---------------- WATERMARK ---------------- */

            if (isUnpaid) {
                addWatermark(writer, "UNPAID");
            }

            /* ---------------- FONTS ---------------- */

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 12);

            /* ---------------- COMPANY HEADER ---------------- */

            Paragraph company = new Paragraph("HARISH E-COMMERCE PVT LTD", boldFont);
            company.setAlignment(Element.ALIGN_LEFT);
            document.add(company);

            document.add(new Paragraph("GSTIN: 22AAAAA0000A1Z5", normalFont));
            document.add(new Paragraph("Email: support@harishecommerce.com", normalFont));
            document.add(new Paragraph("Phone: +91-9999999999", normalFont));
            document.add(new Paragraph(" "));

            /* ---------------- TITLE ---------------- */

            Paragraph invoiceTitle = new Paragraph(title, titleFont);
            invoiceTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(invoiceTitle);
            document.add(new Paragraph(" "));

            /* ---------------- ORDER DETAILS ---------------- */

            String formattedDate = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            document.add(new Paragraph("Invoice No: "
                    + (invoiceNumber != null ? invoiceNumber : "PROFORMA"), boldFont));

            document.add(new Paragraph("Order No: " + order.getId(), normalFont));
            document.add(new Paragraph("Date: " + formattedDate, normalFont));
            document.add(new Paragraph("Customer: "
                    + order.getUser().getUsername(), normalFont));

            document.add(new Paragraph(" "));

            /* ---------------- ITEM TABLE ---------------- */

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3, 1, 1, 1, 1});

            addTableHeader(table);

            for (OrderItem item : order.getOrderItems()) {
                table.addCell(item.getProduct().getProductName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell("₹" + item.getPrice());
                table.addCell("18%");
                table.addCell("₹" + item.getTotalPrice());
            }

            document.add(table);
            document.add(new Paragraph(" "));

            /* ---------------- TOTAL SECTION ---------------- */

            double subtotal = order.getTotalAmount();
            double gst = Math.round(subtotal * 0.18 * 100.0) / 100.0;
            double grandTotal = Math.round((subtotal + gst) * 100.0) / 100.0;

            Paragraph sub = new Paragraph("Subtotal: ₹" + subtotal, boldFont);
            sub.setAlignment(Element.ALIGN_RIGHT);
            document.add(sub);

            Paragraph gstLine = new Paragraph("GST (18%): ₹" + gst, boldFont);
            gstLine.setAlignment(Element.ALIGN_RIGHT);
            document.add(gstLine);

            Paragraph total = new Paragraph("Grand Total: ₹" + grandTotal, boldFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.add(new Paragraph(" "));

            /* ---------------- FOOTER ---------------- */

            Paragraph footer = new Paragraph(
                    "Payment Status: " + (isUnpaid ? "UNPAID" : "PAID") +
                            "\nThis is a system generated invoice.\nThank you for shopping with us!",
                    normalFont);

            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating invoice PDF", e);
        }
    }

    /* ---------------- WATERMARK METHOD ---------------- */

    private void addWatermark(PdfWriter writer, String watermarkText) {

        PdfContentByte canvas = writer.getDirectContentUnder();

        Font watermarkFont = new Font(Font.HELVETICA, 60, Font.BOLD, Color.LIGHT_GRAY);
        Phrase phrase = new Phrase(watermarkText, watermarkFont);

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                phrase,
                297,   // center X (A4 width / 2)
                421,   // center Y (A4 height / 2)
                45     // rotate 45 degrees
        );
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Product", "Qty", "Price", "GST", "Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }
}
