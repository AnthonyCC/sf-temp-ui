
<#macro link id>
	<@ilink node=getNode(id) />
</#macro>

<#macro ilink node>
	<a href="${getHref(node)}">${node.fullName?html}</a></#macro>

<#macro image image alt="">
	<img src="${image.path}" width="${image.width}" height="${image.height}" alt="${alt}" hspace="0" border="0">
</#macro>

<#macro ipod node>
	<#assign type=node.contentKey.type.name/>
	<#assign href=getHref(node)/>
	<#assign label=node.fullName?html/>
    <a href="${href}"><@image image=getDefaultImage(node) alt=label/></a>
    <br>
    <a href="${href}"><span class="catPageProdNameUnderImg">${label}</span></a>
	<#if type="Product">
        <br><span class="price">$${node.defaultPrice}</span>
	</#if>
   <br><br>
</#macro>

<#macro pod id>
	<table>
		<tr><td align="center">
			<@ipod node=getNode(id) />
		</td></tr>
	</table>
</#macro>

<#macro links ids max=99>
	<#assign nodes=retainAvailable(getNodes(ids), max)/>
	<#if nodes?size != 0>
		<#list nodes as node>
				<@ilink node=node/><#if node_has_next><#if node_index = nodes?size-2> and <#else>, </#if></#if>
		</#list>
	<#else>
		<#nested/>
	</#if>
</#macro>

<#macro pods ids max=99 break=5>
	<#assign nodes=retainAvailable(getNodes(ids), max)/>
	<#if nodes?size != 0>
		<#assign cellWidth = "${100/break}%" />
		<table>
			<#list nodes?chunk(break, "") as row>
				<tr>
					<#list row as node>
						<td width="{$cellWidth}" align="center" valign="top">
							<#if node="">
								&nbsp;
							<#else>
								<@ipod node=node/>
							</#if>
						</td>
					</#list>
				</tr>
			</#list>
		</table>
	<#else>
		<#nested/>
	</#if>
</#macro>




