<#assign urlPrefix=parameters["baseUrl"]/>

<#macro link id trk="">
	<@ilink node=getNode(id) trk=trk />
</#macro>

<#macro ilink node trk="">
	<a href="${urlPrefix}${getHref(node, trk)}" name="${node.fullName?html}" style="color:#336600;">${node.fullName?html}</a></#macro>

<#macro image image alt=""><img src="${urlPrefix}${image.path}" width="${image.width}" height="${image.height}" alt="${alt}" hspace="0" border="0"></#macro>

<#macro ipod node trk="">
	<#assign type=node.contentKey.type.name/>
	<#assign href=getHref(node, trk)/>
	<#assign label=node.fullName?html/>
    <a href="${href}" name="${label}"><@image image=getDefaultImage(node) alt="${label}"/></a>
    <br>
    <a href="${href}" name="${label}" style="color:#336600;"><span class="catPageProdNameUnderImg">${label}</span></a>
	
	<#if type="Product">
        <br><span class="price">$${node.defaultPrice}</span>
	</#if>
   <br><br>
</#macro>

<#macro pod id trk="">
	<table>
		<tr><td align="center">
			<@ipod node=getNode(id) trk=trk/>
		</td></tr>
	</table>
</#macro>

<#macro links ids max=99 separator="," trk="">
	<#assign nodes=retainAvailable(getNodes(ids), max)/>
	<#if nodes?size != 0>
		<#list nodes as node>
			<@ilink node=node trk=trk/><#if node_has_next><#if separator=","><#if node_index = nodes?size-2> and <#else>, </#if><#else>${separator}</#if></#if>
		</#list>
	<#else>
		<#nested/>
	</#if>
</#macro>

<#macro pods ids max=99 break=5 trk="">
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
								<@ipod node=node trk=trk/>
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




