<%@
		attribute name="main" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="left1" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="right1" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="left2" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="right2" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="left3" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="right3" required="true" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="mainTopBar" required="false" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="full" required="false" rtexprvalue="true" type="java.lang.String" %><%@
		attribute name="fullTopBar" required="false" rtexprvalue="true" type="java.lang.String" %>

    <div class="oas-homepageredesign">
      <div id='oas_<%= fullTopBar %>'>
        <script type="text/javascript">
          OAS_AD('<%= fullTopBar %>');
        </script>
      </div>
      <div id='oas_<%= full %>'>
        <script type="text/javascript">
        OAS_AD('<%= full %>');
        </script>
      </div>
      <div class="main-top-bar full" ad-size-height='80' ad-size-width='970' ad-fixed-size='true' id='oas_<%= mainTopBar %>'>
        <script type="text/javascript">
          OAS_AD('<%= mainTopBar %>');
        </script>
      </div>
      <div class="oas-container full">
        <div class="main half" ad-size-height='480' ad-size-width='480' ad-fixed-size='true' id='oas_<%= main %>'>
          <script type="text/javascript">
          OAS_AD('<%= main %>');
          </script>
        </div>
        <div class="right-side half">
          <div class="line">
            <div class="half" ad-size-height='154' ad-size-width='240' ad-fixed-size='true' id='oas_<%= left1 %>'>
              <script type="text/javascript">
              OAS_AD('<%= left1 %>');
              </script>
            </div>
            <div class="half right" ad-size-height='154' ad-size-width='240' ad-fixed-size='true' id='oas_<%= right1 %>'>
              <script type="text/javascript">
              OAS_AD('<%= right1 %>');
              </script>
            </div>
          </div>
          <div class="line">
            <div class="half" ad-size-height='154' ad-size-width='240' ad-fixed-size='true' id='oas_<%= left2 %>'>
              <script type="text/javascript">
              OAS_AD('<%= left2 %>');
              </script>
            </div>
            <div class="half right" ad-size-height='154' ad-size-width='240' ad-fixed-size='true' id='oas_<%= right2 %>'>
              <script type="text/javascript">
              OAS_AD('<%= right2 %>');
              </script>
            </div>
          </div>
          <div class="line">
            <div class="half" ad-size-height='154' ad-size-width='240' ad-fixed-size='true' id='oas_<%= left3 %>'>
              <script type="text/javascript">
              OAS_AD('<%= left3 %>');
              </script>
            </div>
            <div class="half right" ad-size-height='154' ad-size-width='240' ad-fixed-size='true' id='oas_<%= right3 %>'>
              <script type="text/javascript">
              OAS_AD('<%= right3 %>');
              </script>
            </div>
          </div>
        </div>
      </div>
    </div>
