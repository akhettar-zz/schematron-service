<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/svrl:schematron-output">
		<report>
			<xsl:apply-templates select ="svrl:failed-assert" />
			<xsl:apply-templates select ="svrl:successful-report" />
		</report>
	</xsl:template>
	
	<xsl:template match="svrl:failed-assert" >
	 	<xsl:element name="message">
	 		<xsl:attribute name="id"><xsl:value-of select ="@id" /></xsl:attribute>
	 		<xsl:attribute name="location"><xsl:value-of select ="@location" /></xsl:attribute>
	 		<xsl:attribute name="line"><xsl:value-of select ="@line-number" /></xsl:attribute>
	 		<xsl:attribute name="column"><xsl:value-of select ="@column-number" /></xsl:attribute>
	 		<xsl:attribute name="validatedBy">schematron</xsl:attribute>
	 		<xsl:value-of select="svrl:text/text()" />
	 	</xsl:element>
	</xsl:template>
	<xsl:template match="svrl:successful-report" >
	 	<xsl:element name="message">
	 		<xsl:attribute name="id"><xsl:value-of select ="@id" /></xsl:attribute>
	 		<xsl:attribute name="location"><xsl:value-of select ="@location" /></xsl:attribute>
	 		<xsl:attribute name="line"><xsl:value-of select ="@line-number" /></xsl:attribute>
	 		<xsl:attribute name="column"><xsl:value-of select ="@column-number" /></xsl:attribute>
	 		<xsl:attribute name="validatedBy">schematron</xsl:attribute>
	 		<xsl:value-of select="svrl:text/text()" />
	 	</xsl:element>
	</xsl:template>
</xsl:stylesheet>

