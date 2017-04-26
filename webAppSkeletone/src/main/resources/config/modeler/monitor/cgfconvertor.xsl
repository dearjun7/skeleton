<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:lxslt="http://xml.apache.org/xslt"
		xmlns:xlink="http://www.w3.org/1999/xlink/"
		xmlns:v="urn:schemas-microsoft-com:vml"		
		version="1.0">

<xsl:output method="html" indent="yes"/>
<xsl:param name="browser" select="ns"/>
<xsl:param name="leftmargin" select="0"/>
<xsl:param name="imageTopmargin" select="0"/>
<xsl:param name="topmargin" select="0"/>
<xsl:param name="imageDir" select=""/>
<xsl:param name="contextPath" select=""/>

<xsl:param name="processid" select="/cgf/@procid" />
<xsl:param name="version" select="/cgf/@ver" />
<xsl:param name="delafter"/>

<xsl:variable name="defIconDirURI" select="concat($imageDir, '/img')"/>

<xsl:variable name="arrowLeftOffset" select="1"/>
<xsl:variable name="arrowTopOffset" select="-30"/>
<xsl:variable name="arrowWidth" select="14"/>
<xsl:variable name="arrowHeight" select="14"/>
<xsl:variable name="left"/>
<xsl:variable name="top"/>
<xsl:variable name="width"/>
<xsl:variable name="height"/>
 

<xsl:template match="/">
	<xsl:apply-templates/>
</xsl:template>


<xsl:template match="cgf">
    <xsl:apply-templates select="monitor"/>
<!-- PalMap Viewer-->
	<svg width='{@width}' height='{@height}'>
		<xsl:apply-templates select="graph//line"/> 
		<xsl:apply-templates select="graph//lineForNS"/> 
		<xsl:apply-templates select="graph//g"/>
		<xsl:apply-templates select="graph//image"/>	
	</svg>
</xsl:template>

<xsl:template match="monitor">
	<div style="z-index:-1;POSITION: absolute; OVERFLOW: visible; LEFT: {$leftmargin}px; TOP: 0px; WIDTH: {@width}px; HEIGHT: {@height}px">
		<!-- <img WIDTH="{@width}" HEIGHT="{@height}" src="{$contextPath}/servlet/streamwriter?delafter={$delafter}&#38;dispfile={@xlink:name}&#38;file={@xlink:href}" border="0" alt=""/> --> 
		<img WIDTH="{@width}" HEIGHT="{@height}" src="{$contextPath}/{@xlink:name}" border="0" alt=""/>
	</div>
</xsl:template>

<xsl:template match="image">
	<div id="{@id}" style="POSITION: absolute; OVERFLOW: visible; LEFT: {$leftmargin+number(@x)}px; TOP: {$imageTopmargin+@y}px; WIDTH: {@width}px; HEIGHT: {@height}px"><img WIDTH="{@width}" HEIGHT="{@height}" src="{@xlink:href}" border="0" alt=""/></div>
</xsl:template>

<xsl:template match="lineForNS">
	<xsl:call-template name="LINE_NS"/>	
</xsl:template>

<xsl:template match="line">
	<xsl:call-template name="LINE"/>
</xsl:template>

<xsl:template name="LINE_NS">
        <xsl:variable name="x1" select="number(@x1)"/>
        <xsl:variable name="y1" select="number(@y1)"/>
        <xsl:variable name="x2" select="number(@x2)"/>
        <xsl:variable name="y2" select="number(@y2)"/>
	<g>
    	<!-- <line style="z-index:101;cursor:pointer;stroke-opacity: 0.1;" id="svg_1" y2='{@y2}' x2='{@x2}' y1='{@y1}' x1='{@x1}' stroke-width="5" stroke="#000000" fill="none" onclick="transitionOnClick('{../../../../../../@procid}', '{../../../../../../@ver}','{../../@id}')"/>-->
        <line style="z-index:104;cursor:pointer;stroke-opacity: 0.1;" id="svg_1" y2='{@y2}' x2='{@x2}' y1='{@y1}' x1='{@x1}' stroke-width="2" stroke="#000000" fill="none" 
        onclick="window.open('#/popup-transition-detail/procs/{$processid}/{$version}/proctranses/{../../@id}', '_blank', 'width=720,height=700')"/>
	</g>
</xsl:template>

<xsl:template name="LINE">
        <xsl:variable name="x1" select="number(@x1)"/>
        <xsl:variable name="y1" select="number(@y1)"/>
        <xsl:variable name="x2" select="number(@x2)"/>
        <xsl:variable name="y2" select="number(@y2)"/>
	<g>
		<line style="z-index:101;cursor:pointer;stroke-opacity: 0.1;" id="svg_1" y2='{@y2}' x2='{@x2}' y1='{@y1}' x1='{@x1}' stroke-width="5" stroke="#000000" fill="none" 
		onclick="window.open('#/popup-transition-detail/procs/{$processid}/{$version}/proctranses/{../../@id}', '_blank', 'width=720,height=700')"/>
	</g>
</xsl:template>


<xsl:template name="string-replace" >
  <xsl:param name="string"/>
  <xsl:param name="from"/>
  <xsl:param name="to"/>
  <xsl:choose>
    <xsl:when test="contains($string,$from)">
      <xsl:value-of select="substring-before($string,$from)"/>
      <xsl:copy-of select="$to"/>
      <xsl:call-template name="string-replace">
      <xsl:with-param name="string"
select="substring-after($string,$from)"/>
      <xsl:with-param name="from" select="$from"/>
      <xsl:with-param name="to" select="$to"/>
      </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="$string"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="ConvertDecToHex">
    <xsl:param name="index" />
    <xsl:if test="$index > 0">
      <xsl:call-template name="ConvertDecToHex">
        <xsl:with-param name="index" select="floor($index div 16)" />
      </xsl:call-template>
      <xsl:choose>
        <xsl:when test="$index mod 16 &lt; 10">
          <xsl:value-of select="$index mod 16" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:choose>
            <xsl:when test="$index mod 16 = 10">A</xsl:when>
            <xsl:when test="$index mod 16 = 11">B</xsl:when>
            <xsl:when test="$index mod 16 = 12">C</xsl:when>
            <xsl:when test="$index mod 16 = 13">D</xsl:when>
            <xsl:when test="$index mod 16 = 14">E</xsl:when>
            <xsl:when test="$index mod 16 = 15">F</xsl:when>
            <xsl:otherwise>A</xsl:otherwise>
          </xsl:choose>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
</xsl:template>

<xsl:template name="ColorHexStr">
	<xsl:param name="hexval"/>
	<xsl:variable name="len" select="string-length($hexval)"/>
	<xsl:variable name="reststr">
		<xsl:choose>
			<xsl:when test="$len = 0">
				<xsl:value-of select="'000000'"/>
			</xsl:when>
			<xsl:when test="$len = 1">
			    <xsl:value-of select="'00000'"/>
			</xsl:when>
			<xsl:when test="$len = 2">
			    <xsl:value-of select="'0000'"/>
			</xsl:when>
			<xsl:when test="$len = 3">
			    <xsl:value-of select="'000'"/>
			</xsl:when>
			<xsl:when test="$len = 4">
			    <xsl:value-of select="'00'"/>
			</xsl:when>
			<xsl:when test="$len = 5">
			    <xsl:value-of select="'0'"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="'000000'"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:value-of select="concat('#',$hexval, $reststr)"/>
</xsl:template>
  
  
<xsl:template match="text">
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@top"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@bottom"/>
	<xsl:variable name="faceName" select="@faceName"/>
	<xsl:variable name="pointSize" select="@pointSize"/>
	<xsl:variable name="font_Color" select="@font_Color"/>
	<xsl:variable name="font_Weight" select="@font_Weight"/>
	<xsl:variable name="font_Italic" select="@font_Italic"/>
	<xsl:variable name="font_Underline" select="@font_Underline"/>
	<xsl:variable name="font_Strikeout" select="@font_Strikeout"/>
		
	<xsl:variable name="fc">
		<xsl:call-template name="ColorHexStr">
			<xsl:with-param name="hexval">
				<xsl:call-template name="ConvertDecToHex">
					<xsl:with-param name="index" select="$font_Color" />
				</xsl:call-template>	
			</xsl:with-param>
		</xsl:call-template>
	</xsl:variable>		
	
	<xsl:variable name="fd">
		<xsl:choose>
			<xsl:when test="$font_Underline = '1' and $font_Strikeout = '1'">
				<xsl:value-of select="string('underline line-through')"/>
			</xsl:when>
			<xsl:when test="$font_Underline = '0' and $font_Strikeout = '1'">
				<xsl:value-of select="line-through"/>
			</xsl:when>
			<xsl:when test="$font_Underline = '1' and $font_Strikeout = '0'">
				<xsl:value-of select="underline"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="none"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<xsl:variable name="fs">
		<xsl:choose>
			<xsl:when test="$pointSize = 6">
				<xsl:value-of select="'8px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 7">
				<xsl:value-of select="'9px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 7.5">
				<xsl:value-of select="'10px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 8">
				<xsl:value-of select="'11px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 9">
				<xsl:value-of select="'12px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 10">
				<xsl:value-of select="'13px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 10.5">
				<xsl:value-of select="'14px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 11">
				<xsl:value-of select="'15px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 12">
				<xsl:value-of select="'16px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 13">
				<xsl:value-of select="'17px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 13.5">
				<xsl:value-of select="'18px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 14">
				<xsl:value-of select="'19px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 14.5">
				<xsl:value-of select="'20px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 15">
				<xsl:value-of select="'21px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 16">
				<xsl:value-of select="'22px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 17">
				<xsl:value-of select="'23px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 18">
				<xsl:value-of select="'24px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 19">
				<xsl:value-of select="'25px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 20">
				<xsl:value-of select="'26px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 21">
				<xsl:value-of select="'27px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 22">
				<xsl:value-of select="'29px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 23">
				<xsl:value-of select="'30px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 24">
				<xsl:value-of select="'32px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 25">
				<xsl:value-of select="'33px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 26">
				<xsl:value-of select="'35px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 27">
				<xsl:value-of select="'36px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 28">
				<xsl:value-of select="'37px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 29">
				<xsl:value-of select="'38px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 30">
				<xsl:value-of select="'40px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 31">
				<xsl:value-of select="'41px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 32">
				<xsl:value-of select="'42px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 33">
				<xsl:value-of select="'43px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 34">
				<xsl:value-of select="'45px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 35">
				<xsl:value-of select="'46px'"/>
			</xsl:when>
			<xsl:when test="$pointSize = 36">
				<xsl:value-of select="'48px'"/>
			</xsl:when>
		
		</xsl:choose>
	</xsl:variable>
	
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y)}px; WIDTH:{number($x2)-number($x)}px; HEIGHT:{number($y2)-number($y)}px;color:{$fc};font-family:{$faceName};font-weight:{$font_Weight};text-decoration:{$fd};font-size:{$fs};line-height:125%;">	
	<center>
		<xsl:choose>
			<xsl:when test="$font_Italic = '1'">
				<i><xsl:value-of select="."/></i>
			</xsl:when>
			<xsl:otherwise>
			<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>	 	
	 </center>  
	</div> 
</xsl:template>

<xsl:template match="g">

	<xsl:choose>
		<xsl:when test="@paletteType != '900' and @paletteType != '901' and @paletteType != '902' and @paletteType != '903' and @paletteType != '31101' and @paletteType != '802' and @paletteType != '803' and @paletteType != '804' and @paletteType != '805' and @paletteType != '806' and @paletteType != '807'">		
			<xsl:apply-templates select="text"/>
		</xsl:when>
	</xsl:choose>	
	
	<xsl:choose>
<!-- PalMap Viewer -->
		<xsl:when test="@paletteType = '31001'">
			<xsl:call-template name="START-EVENT"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31002'">
			<xsl:call-template name="END-EVENT"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31021'">
			<xsl:call-template name="ACTOR"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31022'">
			<xsl:call-template name="EVENT"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31071'">
			<xsl:call-template name="XOR-GATEWAY"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31072'">
			<xsl:call-template name="OR-GATEWAY"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31073'">
			<xsl:call-template name="AND-GATEWAY"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31074'">
			<xsl:call-template name="CPX-GATEWAY"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31041'">
			<xsl:call-template name="TERM-END"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31042'">
			<xsl:call-template name="DEMOL-END"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31023'">
			<xsl:call-template name="DATABASE"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31024'">
			<xsl:call-template name="DATA-FILE"/>
		</xsl:when>

		<xsl:when test="@paletteType = '31062'">
			<xsl:call-template name="EXT-SUBPROCESS"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31061'">
			<xsl:call-template name="EXT-NORMAL"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31052'">
			<xsl:call-template name="SUBPROCESS"/>
		</xsl:when>
		<xsl:when test="@paletteType = '31051'">
		    <xsl:call-template name="NORMAL"/>
		</xsl:when>
	</xsl:choose>
</xsl:template>

<!-- PalMap Viewer -->
<!-- <xsl:template name="ARROW"> -->
<!--	:	-->
<!-- </xsl:template>	-->

<xsl:template name="START-EVENT">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' 
		onclick="window.open('#/popup-notation-detail/procs/{$processid}/{$version}/procacts/{@id}', '_blank', 'width=720,height=700')"/> 
	</div>
<!--  	
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -25}px; WIDTH: 24px; HEIGHT: 42px;">
		<img WIDTH="24" HEIGHT="42" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:starteventOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
	 -->
</xsl:template>

<xsl:template name="END-EVENT">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' 
		onclick="window.open('#/popup-notation-detail/procs/{$processid}/{$version}/procacts/{@id}', '_blank', 'width=720,height=700')"/> 
	</div>
<!--  	
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -25}px; WIDTH: 24px; HEIGHT: 42px;">
		<img WIDTH="24" HEIGHT="42" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:endeventOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
	--> 
</xsl:template>

<xsl:template name="ACTOR">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:actorOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
<!--  	
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -25}px; WIDTH: 24px; HEIGHT: 42px;">
		<img WIDTH="24" HEIGHT="42" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:actorOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
	 -->
</xsl:template>

<xsl:template name="EVENT">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:eventOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
<!--  	
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -25}px; WIDTH: 24px; HEIGHT: 42px;">
		<img WIDTH="24" HEIGHT="42" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:eventOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
	--> 
</xsl:template>

<xsl:template name="XOR-GATEWAY">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' 
		onclick="window.open('#/popup-notation-detail/procs/{$processid}/{$version}/procacts/{@id}', '_blank', 'width=720,height=700')"/> 
	</div>
<!--  	
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -25}px; WIDTH: 24px; HEIGHT: 42px;">
		<img WIDTH="24" HEIGHT="42" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:xorgatewayOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
	 -->
</xsl:template>
<xsl:template name="OR-GATEWAY">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' 
		onclick="window.open('#/popup-notation-detail/procs/{$processid}/{$version}/procacts/{@id}', '_blank', 'width=720,height=700')"/> 
	</div>
<!--  	
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -25}px; WIDTH: 24px; HEIGHT: 42px;">
		<img WIDTH="24" HEIGHT="42" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:orgatewayOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
	--> 
</xsl:template>
<xsl:template name="AND-GATEWAY">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' 
		onclick="window.open('#/popup-notation-detail/procs/{$processid}/{$version}/procacts/{@id}', '_blank', 'width=720,height=700')"/> 
	</div>
<!--  	
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -25}px; WIDTH: 24px; HEIGHT: 42px;">
		<img WIDTH="24" HEIGHT="42" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:andgatewayOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
	--> 
</xsl:template>
<xsl:template name="CPX-GATEWAY">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' 
		onclick="window.open('#/popup-notation-detail/procs/{$processid}/{$version}/procacts/{@id}', '_blank', 'width=720,height=700')"/> 
	</div>
<!--  
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -25}px; WIDTH: 24px; HEIGHT: 42px;">
		<img WIDTH="24" HEIGHT="42" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:cpxgatewayOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
-->	 
</xsl:template>
<xsl:template name="TERM-END">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:termendOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
<!--  
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -25}px; WIDTH: 24px; HEIGHT: 42px;">
		<img WIDTH="24" HEIGHT="42" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:termendOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
-->	 
</xsl:template>
<xsl:template name="DEMOL-END">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:demolendOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
<!--  
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -25}px; WIDTH: 24px; HEIGHT: 42px;">
		<img WIDTH="24" HEIGHT="42" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:demolendOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
-->	 
</xsl:template>
<xsl:template name="DATABASE">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' 
		onclick="window.open('#/popup-notation-detail/procs/{$processid}/{$version}/procacts/{@id}', '_blank', 'width=720,height=700')"/> 
	</div>
<!--  
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -25}px; WIDTH: 24px; HEIGHT: 42px;">
		<img WIDTH="24" HEIGHT="42" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:databaseOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
-->
</xsl:template>

<xsl:template name="DATA-FILE">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' 
		onclick="window.open('#/popup-notation-detail/procs/{$processid}/{$version}/procacts/{@id}', '_blank', 'width=720,height=700')"/> 
	</div>
<!--  
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -25}px; WIDTH: 24px; HEIGHT: 42px;">
		<img WIDTH="24" HEIGHT="42" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:datafileOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
-->	 
</xsl:template>

<xsl:template name="EXT-SUBPROCESS">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
<!-- PalMap Viewer -->
	<div
		style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}"
			src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer'
			onclick="window.open('#/popup-notation-detail/procs/{$processid}/{$version}/procacts/{@id}', '_blank', 'width=720,height=700')"/>
	</div>
	
<!--  
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -50}px; WIDTH: 72px; HEIGHT: 60px;">
		<img WIDTH="72" HEIGHT="60" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:extsubprocessOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
-->	 
</xsl:template>

<xsl:template name="EXT-NORMAL">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>
	
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' 
			onclick="window.open('#/popup-notation-detail/procs/{$processid}/{$version}/procacts/{@id}', '_blank', 'width=720,height=700')"/> 
	</div>
<!--  
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -50}px; WIDTH: 72px; HEIGHT: 60px;">
		<img WIDTH="72" HEIGHT="60" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:extnormalActivityOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
-->	 
</xsl:template>

<xsl:template name="SUBPROCESS">
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>	
	
<!-- PalMap Viewer -->
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' 
			onclick="window.open('#/popup-notation-detail/procs/{$processid}/{$version}/procacts/{@id}', '_blank', 'width=720,height=700')"/> 
	</div>
<!--  
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -50}px; WIDTH: 72px; HEIGHT: 60px;">
		<img WIDTH="72" HEIGHT="60" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:subprocessOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>
-->	 
</xsl:template>

<xsl:template name="NORMAL">
<!-- PalMap Viewer -->
	<xsl:variable name="uri" select="concat($defIconDirURI, '/blank.gif')"/>
	<xsl:variable name="x" select="@left"/>
	<xsl:variable name="y" select="@bottom"/>
	<xsl:variable name="x2" select="@right"/>
	<xsl:variable name="y2" select="@top"/>	
  
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y2)}px; WIDTH: {number($x2)-number($x)}px; HEIGHT: {number($y)-number($y2)}px;">
		<img WIDTH="{number($x2)-number($x)}" HEIGHT="{number($y)-number($y2)}" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' 
			onclick="window.open('#/popup-notation-detail/procs/{$processid}/{$version}/procacts/{@id}', '_blank', 'width=720,height=700')"/> 
			
	</div>

<!--      	
	<div style="z-index:1;POSITION: absolute; OVERFLOW: visible; LEFT: {number($x)}px; TOP: {number($y) -50}px; WIDTH: 72px; HEIGHT: 60px;">
		<img WIDTH="72" HEIGHT="60" src="{$uri}" border="0" alt="{$uri}" style='cursor:pointer' onclick="javascript:normalActivityOnClick('{$processid}', '{$version}','{@id}');"/> 
	</div>	
-->
</xsl:template>

<!-- PAL Map Viewer (노테이선 이름이 표시되지 않도록)-->
<!--<xsl:template match="g/text[@style]">	-->
<!--          :        -->
<!--</xsl:template> -->

<!-- PAL Map Viewer (노테이선 이름이 표시되지 않도록)-->
<!--<xsl:template match="g[@activityType='C']/text[@style]">	-->
<!--          :        -->
<!--</xsl:template> -->

<!-- PAL Map Viewer (노테이선 이름이 표시되지 않도록)-->
<!--<xsl:template match="text">	-->
<!--          :        -->
<!--</xsl:template> -->

<!-- PAL Map Viewer (노테이선 이름이 표시되지 않도록)-->
<!--<xsl:template match="g/node[@style]">	-->
<!--          :        -->
<!--</xsl:template> -->

</xsl:stylesheet>