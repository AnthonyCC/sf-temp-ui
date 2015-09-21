'$Id: Utils.vbs 174 2012-01-10 08:26:10Z 501349 $
'Copyright 2009 iGATE GROUP OF COMPANIES. All rights reserved
'(Subject to Limited Distribution and Restricted Disclosure Only.)
'THIS SOURCE FILE MAY CONTAIN INFORMATION WHICH IS THE PROPRIETARY
'information of IGATE GROUP OF COMPANIES AND IS INTENDED FOR USE
'ONLY BY THE ENTITY WHO IS ENTITLED TO AND MAY CONTAIN
'INFORMATION THAT IS PRIVILEGED, CONFIDENTIAL, OR EXEMPT FROM
'DISCLOSURE UNDER APPLICABLE LAW.
'YOUR ACCESS TO THIS SOURCE FILE IS GOVERNED BY THE TERMS AND
'CONDITIONS OF AN AGREEMENT BETWEEN YOU AND IGATE GROUP OF COMPANIES.
'The USE, DISCLOSURE REPRODUCTION OR TRANSFER OF THIS PROGRAM IS
'RESTRICTED AS SET FORTH THEREIN.

Option Explicit

Dim oUTILS
Set oUTILS = New Utils

' Class Name: Utils
' Description: 
Class Utils

  Private Function GetTimeZoneOffset()
    Const sComputer = "."

    Dim oWmiService : Set oWmiService = _
        GetObject("winmgmts:{impersonationLevel=impersonate}!\\" _
                  & sComputer & "\root\cimv2")
    Dim cItems
    Set cItems = oWmiService.ExecQuery("SELECT * FROM Win32_ComputerSystem")
    Dim oItem
    For Each oItem In cItems
        GetTimeZoneOffset = oItem.CurrentTimeZone*60
        Exit For
    Next
  End Function

  Function ToTimeStamp(dt)
    ToTimeStamp = DateDiff("s", epochDt, dt)
  End Function

  Function TsToTime(ts)
    Dim dt
    dt = TsToDate(ts)
    TsToTime = (FormatDateTime(dt,3))
  End Function

  Function TsToDate(ts)
    TsToDate = CDate(DateAdd("s", ts, epochDt))
  End Function

  Function FrmtDate(ts)
    FrmtDate = FormatDateTime(TsToDate(ts),vbGeneralDate)
  End Function
  
	Dim oSysEnv
  Dim epochDt

  Private Function getEpochDt()
    getEpochDt = DateAdd("s", GetTimeZoneOffset,CDate("01/01/1970 0:0:0"))
  End Function
  
	Private Sub Class_Initialize
		Dim oShell
		Set oShell = CreateObject("WScript.Shell")
		Set oSysEnv= oShell.Environment("system")
    epochDt = getEpochDt
	End Sub
	Private Sub Class_Terminate
		Set oSysEnv = Nothing
	End Sub
End Class ' End of Utils Class


