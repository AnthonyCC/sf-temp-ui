-- (Credit Issue) Reason to (Case) Subject mapping provided by Harry Orenstein
--
-- CRM Enhancements Feature #1
--
update complaint_code set subject_code='ORQ-012' where code='PRDTMP';
update complaint_code set priority='1' where code='PRDTMP';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Damaged') where code='PRDTMP';
update complaint_code set subject_code='ORQ-012' where code='ADVERSE';
update complaint_code set priority='1' where code='ADVERSE';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='ADVERSE';
update complaint_code set subject_code='ORQ-012' where code='DIRT';
update complaint_code set priority='1' where code='DIRT';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='DIRT';
update complaint_code set subject_code='ORQ-012' where code='FOROBJ';
update complaint_code set priority='1' where code='FOROBJ';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='FOROBJ';
update complaint_code set subject_code='ORQ-012' where code='INSECT';
update complaint_code set priority='1' where code='INSECT';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='INSECT';
update complaint_code set subject_code='ORQ-012' where code='NTCLND';
update complaint_code set priority='1' where code='NTCLND';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='NTCLND';
update complaint_code set subject_code='ORQ-012' where code='WORMS';
update complaint_code set priority='1' where code='WORMS';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='WORMS';
update complaint_code set subject_code='TCQ-003' where code='BLDGDAM';
update complaint_code set priority='2' where code='BLDGDAM';
update complaint_code set subject_code='TCQ-003' where code='PROPDAM';
update complaint_code set priority='2' where code='PROPDAM';
update complaint_code set subject_code='TCQ-004' where code='ACCDNT';
update complaint_code set priority='3' where code='ACCDNT';
update complaint_code set subject_code='TCQ-001' where code='DRVSMELL';
update complaint_code set priority='4' where code='DRVSMELL';
update complaint_code set subject_code='TCQ-001' where code='DRVMISH';
update complaint_code set priority='4' where code='DRVMISH';
update complaint_code set subject_code='TCQ-001' where code='DRVRUDE';
update complaint_code set priority='4' where code='DRVRUDE';
update complaint_code set subject_code='DSQ-014' where code='NODEL';
update complaint_code set priority='5' where code='NODEL';
update complaint_code set subject_code='DPQ-007' where code='DPPOORXP';
update complaint_code set priority='5' where code='DPPOORXP';
update complaint_code set subject_code='ORQ-011' where code='DOORMAN';
update complaint_code set priority='6' where code='DOORMAN';
update complaint_code set subject_code='ORQ-001' where code='MISBOX';
update complaint_code set priority='7' where code='MISBOX';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Missing') where code='MISBOX';
update complaint_code set subject_code='ORQ-001' where code='WRNBOX';
update complaint_code set priority='7' where code='WRNBOX';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Missing') where code='WRNBOX';
update complaint_code set subject_code='ORQ-002' where code='MISFRZ';
update complaint_code set priority='8' where code='MISFRZ';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Missing') where code='MISFRZ';
update complaint_code set subject_code='ORQ-007' where code='DAMBOX';
update complaint_code set priority='9' where code='DAMBOX';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Damaged') where code='DAMBOX';
update complaint_code set subject_code='ORQ-004' where code='RTLBWRBX';
update complaint_code set priority='10' where code='RTLBWRBX';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Missing') where code='RTLBWRBX';
update complaint_code set subject_code='DPQ-007' where code='DPAUTODL';
update complaint_code set priority='11' where code='DPAUTODL';
update complaint_code set subject_code='DPQ-007' where code='DPAUTOCX';
update complaint_code set priority='11' where code='DPAUTOCX';
update complaint_code set subject_code='DPQ-007' where code='DPWRONG';
update complaint_code set priority='11' where code='DPWRONG';
update complaint_code set subject_code='ORQ-013' where code='EARLDEL';
update complaint_code set priority='12' where code='EARLDEL';
update complaint_code set subject_code='DSQ-016' where code='DLVPRB';
update complaint_code set priority='13' where code='DLVPRB';
update complaint_code set subject_code='LDQ-004' where code='LATEDL0';
update complaint_code set priority='14' where code='LATEDL0';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Late') where code='LATEDL0';
update complaint_code set subject_code='LDQ-005' where code='LATEDL30';
update complaint_code set priority='15' where code='LATEDL30';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Late') where code='LATEDL30';
update complaint_code set subject_code='LDQ-010' where code='LATEDL60';
update complaint_code set priority='16' where code='LATEDL60';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Late') where code='LATEDL60';
update complaint_code set subject_code='LDQ-006' where code='LATEDL90';
update complaint_code set priority='17' where code='LATEDL90';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Late') where code='LATEDL90';
update complaint_code set subject_code='ORQ-006' where code='PRDXCL';
update complaint_code set priority='18' where code='PRDXCL';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='PRDXCL';
update complaint_code set subject_code='ORQ-006' where code='PRDEXP';
update complaint_code set priority='18' where code='PRDEXP';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='PRDEXP';
update complaint_code set subject_code='ORQ-007' where code='AVDFZN';
update complaint_code set priority='19' where code='AVDFZN';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Damaged') where code='AVDFZN';
update complaint_code set subject_code='ORQ-007' where code='WARM';
update complaint_code set priority='19' where code='WARM';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Damaged') where code='WARM';
update complaint_code set subject_code='ORQ-008' where code='DEFROST';
update complaint_code set priority='20' where code='DEFROST';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Damaged') where code='DEFROST';
update complaint_code set subject_code='ORQ-005' where code='PITTS';
update complaint_code set priority='21' where code='PITTS';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='PITTS';
update complaint_code set subject_code='ORQ-006' where code='SMELL';
update complaint_code set priority='22' where code='SMELL';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='SMELL';
update complaint_code set subject_code='ORQ-006' where code='DISCOL';
update complaint_code set priority='22' where code='DISCOL';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='DISCOL';
update complaint_code set subject_code='ORQ-006' where code='XSOFT';
update complaint_code set priority='22' where code='XSOFT';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='XSOFT';
update complaint_code set subject_code='ORQ-006' where code='HRDSTL';
update complaint_code set priority='22' where code='HRDSTL';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='HRDSTL';
update complaint_code set subject_code='ORQ-006' where code='MOLDY';
update complaint_code set priority='22' where code='MOLDY';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='MOLDY';
update complaint_code set subject_code='ORQ-006' where code='ORIPE';
update complaint_code set priority='22' where code='ORIPE';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='ORIPE';
update complaint_code set subject_code='ORQ-006' where code='PRDDED';
update complaint_code set priority='22' where code='PRDDED';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='PRDDED';
update complaint_code set subject_code='ORQ-006' where code='QUALDIS';
update complaint_code set priority='22' where code='QUALDIS';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='QUALDIS';
update complaint_code set subject_code='ORQ-006' where code='SFOPCL';
update complaint_code set priority='22' where code='SFOPCL';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='SFOPCL';
update complaint_code set subject_code='ORQ-006' where code='SPOILED';
update complaint_code set priority='22' where code='SPOILED';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='SPOILED';
update complaint_code set subject_code='ORQ-006' where code='TOOFAT';
update complaint_code set priority='22' where code='TOOFAT';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='TOOFAT';
update complaint_code set subject_code='ORQ-006' where code='TOOGRIS';
update complaint_code set priority='22' where code='TOOGRIS';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='TOOGRIS';
update complaint_code set subject_code='ORQ-006' where code='TOOSPIC';
update complaint_code set priority='22' where code='TOOSPIC';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='TOOSPIC';
update complaint_code set subject_code='ORQ-006' where code='URIPE';
update complaint_code set priority='22' where code='URIPE';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='URIPE';
update complaint_code set subject_code='ORQ-006' where code='WILTED';
update complaint_code set priority='22' where code='WILTED';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='WILTED';
update complaint_code set subject_code='ORQ-007' where code='BRUISED';
update complaint_code set priority='23' where code='BRUISED';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Damaged') where code='BRUISED';
update complaint_code set subject_code='ORQ-007' where code='CRACKED';
update complaint_code set priority='23' where code='CRACKED';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Damaged') where code='CRACKED';
update complaint_code set subject_code='ORQ-007' where code='PRDLEK';
update complaint_code set priority='23' where code='PRDLEK';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Damaged') where code='PRDLEK';
update complaint_code set subject_code='ORQ-004' where code='RTLBWRPR';
update complaint_code set priority='24' where code='RTLBWRPR';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Missing') where code='RTLBWRPR';
update complaint_code set subject_code='ORQ-003' where code='MISITM';
update complaint_code set priority='25' where code='MISITM';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Missing') where code='MISITM';
update complaint_code set subject_code='ORQ-005' where code='DISCUT';
update complaint_code set priority='26' where code='DISCUT';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='DISCUT';
update complaint_code set subject_code='ORQ-005' where code='NTCUBSLC';
update complaint_code set priority='26' where code='NTCUBSLC';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='NTCUBSLC';
update complaint_code set subject_code='ORQ-005' where code='NTGNDSPC';
update complaint_code set priority='26' where code='NTGNDSPC';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='NTGNDSPC';
update complaint_code set subject_code='ORQ-005' where code='NTPAKSPC';
update complaint_code set priority='26' where code='NTPAKSPC';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='NTPAKSPC';
update complaint_code set subject_code='ORQ-005' where code='NTASEX';
update complaint_code set priority='26' where code='NTASEX';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='NTASEX';
update complaint_code set subject_code='ORQ-005' where code='SIZBIG';
update complaint_code set priority='26' where code='SIZBIG';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='SIZBIG';
update complaint_code set subject_code='ORQ-005' where code='SIZSML';
update complaint_code set priority='26' where code='SIZSML';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='SIZSML';
update complaint_code set subject_code='ORQ-005' where code='WRMISPK';
update complaint_code set priority='26' where code='WRMISPK';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Missing') where code='WRMISPK';
update complaint_code set subject_code='ORQ-005' where code='WRNSIZ';
update complaint_code set priority='26' where code='WRNSIZ';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='WRNSIZ';
update complaint_code set subject_code='ORQ-007' where code='DNTCAN';
update complaint_code set priority='27' where code='DNTCAN';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Damaged') where code='DNTCAN';
update complaint_code set subject_code='ORQ-007' where code='DAMBOPRD';
update complaint_code set priority='27' where code='DAMBOPRD';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Damaged') where code='DAMBOPRD';
update complaint_code set subject_code='ORQ-007' where code='PRDDAM';
update complaint_code set priority='27' where code='PRDDAM';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Damaged') where code='PRDDAM';
update complaint_code set subject_code='ORQ-005' where code='BULK';
update complaint_code set priority='28' where code='BULK';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='BULK';
update complaint_code set subject_code='ORQ-005' where code='CUSNOSUB';
update complaint_code set priority='28' where code='CUSNOSUB';
update complaint_code set subject_code='ORQ-005' where code='DISPSIZ';
update complaint_code set priority='28' where code='DISPSIZ';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='DISPSIZ';
update complaint_code set subject_code='ORQ-006' where code='RTGDIS';
update complaint_code set priority='29' where code='RTGDIS';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Quality') where code='RTGDIS';
update complaint_code set subject_code='OUT-007' where code='SRTITM';
update complaint_code set priority='30' where code='SRTITM';
update complaint_code set DLV_ISSUE_CODE=(select code from complaint_dlv_type where name='Missing') where code='SRTITM';
update complaint_code set subject_code='PRQ-011' where code='NTELIG';
update complaint_code set priority='31' where code='NTELIG';
update complaint_code set subject_code='PRQ-009' where code='PRMNTWRK';
update complaint_code set priority='32' where code='PRMNTWRK';
update complaint_code set subject_code='PRQ-008' where code='PRMNTDLV';
update complaint_code set priority='33' where code='PRMNTDLV';
update complaint_code set subject_code='ORQ-009' where code='CUSTERR';
update complaint_code set priority='34' where code='CUSTERR';
update complaint_code set subject_code='DPQ-007' where code='DPMOVE';
update complaint_code set priority='35' where code='DPMOVE';
update complaint_code set subject_code='DSQ-017' where code='UDALCRMV';
update complaint_code set priority='36' where code='UDALCRMV';
update complaint_code set subject_code='DPQ-007' where code='DPDIS';
update complaint_code set priority='37' where code='DPDIS';
update complaint_code set subject_code='PRQ-010' where code='MKTEVT';
update complaint_code set priority='38' where code='MKTEVT';
update complaint_code set subject_code='TCQ-001' where code='MISINV';
update complaint_code set priority='39' where code='MISINV';
update complaint_code set subject_code='CAQ-007' where code='TAXEMPT';
update complaint_code set priority='40' where code='TAXEMPT';
update complaint_code set subject_code='OUT-010' where code='GENDIS';
update complaint_code set priority='41' where code='GENDIS';
update complaint_code set subject_code='OUT-010' where code='GDWILL';
update complaint_code set priority='41' where code='GDWILL';
update complaint_code set subject_code='OUT-010' where code='OTHER';
update complaint_code set priority='41' where code='OTHER';
