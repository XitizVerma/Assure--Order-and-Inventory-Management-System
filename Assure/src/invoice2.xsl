<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <!-- Attribute used for table border -->
    <xsl:attribute-set name="tableBorder">
        <xsl:attribute name="border">solid 0.1mm black</xsl:attribute>
    </xsl:attribute-set>
    <xsl:template match="invoiceData">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simpleA4"
                                       page-height="29.7cm" page-width="25.0cm" margin="1cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simpleA4">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="25pt" text-align="center" font-family="Helvetica" font-weight="bold" space-after="2mm">
                        Invoice
                    </fo:block>
                    <fo:block font-size="12pt" text-align="center" font-family="Helvetica" font-weight="bold" space-after="5mm">
                        Ordered on - <xsl:value-of select="invoiceGenerationTime"/>
                    </fo:block>
                    <fo:block font-size="12pt" text-align="center" font-family="Helvetica" font-weight="bold" space-after="5mm">
                        Order Id-<xsl:value-of select="orderId"/>
                    </fo:block>
                    <fo:block font-size="10pt">
                        <fo:table table-layout="fixed" width="100%" border-collapse="separate">
                            <fo:table-column column-width="2cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-column column-width="3cm"/>
                            <fo:table-column column-width="3cm"/>
                            <fo:table-column column-width="3cm"/>
                            <fo:table-header font-weight="bold">
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">Sr. no.</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">Client Sku Id</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt"  text-align="center" font-weight="bold">Quantity</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">Selling Price</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">Total Price</fo:block>
                                </fo:table-cell>
                            </fo:table-header>
                            <fo:table-body>
                                <xsl:apply-templates select="orderItemDataList"/>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                    <fo:block font-size="15pt" font-family="Helvetica" text-align="right" color="black" font-weight="bold" padding-right="100%" space-after="10mm">
                        Total:<xsl:value-of select="format-number(total, '0.00')"/>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="orderItemDataList">
        <fo:table-row>
            <fo:table-cell  border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="left" font-size="15pt">
                    <xsl:value-of select="position()"/>
                </fo:block>
            </fo:table-cell>

            <fo:table-cell  border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="left" font-size="15pt">
                    <xsl:value-of select="clientSkuId"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell  border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="left" font-size="15pt">
                    <xsl:value-of select="orderedQuantity"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell  border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="left" font-size="15pt">
                    <xsl:value-of select="format-number(sellingPricePerUnit,'0.00')"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell  border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="left" font-size="15pt">
                    <xsl:value-of select="format-number(sellingPrice * orderedQuantity,'0.00')"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>

    </xsl:template>
</xsl:stylesheet>




