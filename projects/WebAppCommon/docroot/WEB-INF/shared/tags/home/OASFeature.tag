<%@
		attribute name="top" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="left" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="tab1" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="tab2" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="tab3" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="tab4" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="bottom" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="hpBottomLeft" required="false" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="hpBottomMiddle" required="false" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="hpBottomRight" required="false" rtexprvalue="true" type="java.lang.String" %>
		<div class="oas_feature_frame" style="padding-top: 10px;" id='oas_<%= top %>'>
			<span>
				<script type="text/javascript">
					OAS_AD('<%= top %>');
					showExtCampaignButtons();
				</script>
			</span>
		</div>
		<div class="oas_feature_left left" id='oas_<%= left %>'>
			<script type="text/javascript">
				OAS_AD('<%= left %>');
			</script>
   		</div>
   		<div class="oas_feature_right right">
   			<div class="oas_feature_right_tab" id='oas_<%= tab1 %>'>
	   			<script type="text/javascript">
					OAS_AD('<%= tab1 %>');
				</script>
			</div>
   			<div class="oas_feature_right_tab" id='oas_<%= tab2 %>'>
	   			<script type="text/javascript">
					OAS_AD('<%= tab2 %>');
				</script>
			</div>
   			<div class="oas_feature_right_tab" id='oas_<%= tab3 %>'>
	   			<script type="text/javascript">
					OAS_AD('<%= tab3 %>');
				</script>
			</div>
   			<div class="oas_feature_right_tab" id='oas_<%= tab4 %>'>
	   			<script type="text/javascript">
					OAS_AD('<%= tab4 %>');
				</script>
			</div>
   		</div>
	   	<div class="clear" style="font-size: 0px;"></div>

		<div class="oas_feature_frame" style="padding-top: 10px;">
			<div class="left" id='oas_<%= hpBottomLeft %>'>
				<script type="text/javascript">
					OAS_AD('<%= hpBottomLeft %>');
				</script>
	   		</div>
			<div class="left" id='oas_<%= hpBottomMiddle %>'>
				<script type="text/javascript">
					OAS_AD('<%= hpBottomMiddle %>');
				</script>
	   		</div>
			<div class="left" id='oas_<%= hpBottomRight %>'>
				<script type="text/javascript">
					OAS_AD('<%= hpBottomRight %>');
				</script>
	   		</div>
		</div>
	   	<div class="clear" style="font-size: 0px;"></div>

		<div class="oas_feature_frame" id='oas_<%= bottom %>'>
			<span>
	   			<script type="text/javascript">
					OAS_AD('<%= bottom %>');
				</script>
			</span>
		</div>
	   	<div class="clear" style="font-size: 0px;"></div>
