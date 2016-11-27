package com.freshdirect.dashboard.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.freshdirect.dashboard.data.response.ForecastResponse;
import com.freshdirect.dashboard.exception.ErrorCodeEventEnum;
import com.freshdirect.dashboard.exception.ServiceException;
import com.freshdirect.dashboard.model.BounceData;
import com.freshdirect.dashboard.model.ForecastModel;
import com.freshdirect.dashboard.model.RollData;
import com.freshdirect.dashboard.service.IEventService;
import com.freshdirect.dashboard.service.IOrderService;
import com.freshdirect.dashboard.util.Base64Decode;
import com.freshdirect.dashboard.util.DateUtil;
import com.freshdirect.dashboard.util.OrderRateUtil;

@Controller
@RequestMapping("/event")
public class EventController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(EventController.class);
	
	@Autowired
	private IEventService eventService;
	
	@Autowired
	private IOrderService orderService;
		
	/**
     * @return the Forecast volume for delivery date
     * @throws ServiceException if the requested date is null.
     */	
	@RequestMapping(value="/bounce", method=RequestMethod.GET)	
	public @ResponseBody ForecastResponse getForcast(HttpServletRequest request, HttpServletResponse response) {
		
		ForecastResponse responseMessage = new ForecastResponse();
		String deliveryDate = request.getParameter("deliveryDate");
		String zone = request.getParameter("zone");
		String shift = request.getParameter("shift");
		if(deliveryDate == null || ("".equals(deliveryDate))) {
			deliveryDate = DateUtil.getNextDate();
		}
		
		try {
			List<BounceData> bounceList = eventService.getBounceByZone(deliveryDate, zone);
			List<RollData> rollList = eventService.getRollByZone(deliveryDate, zone);
			
			Map<Date, String> cutoffMap = orderService.getCutoffs();
			
			Date currentMaxSnapshot = null;
			ForecastModel _tempModel = null;
			int currentMaxRoll = 0, currentMaxBounce = 0;
			if(bounceList != null) {
				for (ListIterator<BounceData> i = bounceList.listIterator(); i.hasNext();) {
					BounceData _bounce = i.next();	
					currentMaxSnapshot = _bounce.getSnapshotTime();
					
					if(_bounce.getZone() == null
							|| (cutoffMap != null 
									&& cutoffMap.get(_bounce.getCutOff()) != null
									&& cutoffMap.get(_bounce.getCutOff()).equals(shift))) {
						
						currentMaxBounce = _bounce.getCnt();
						
						// bounce volume
						_tempModel = new ForecastModel();
						_tempModel.setSnapshotTime(DateUtil.getServerTime(_bounce.getSnapshotTime()));
						_tempModel.setVolume(_bounce.getCnt());
						responseMessage.getBounce().add(_tempModel);
					}
				}
				
				if(currentMaxSnapshot != null) {
					Date maxSnapshot = DateUtil.getMaxSnapshot(currentMaxSnapshot);
					while(currentMaxSnapshot.before(maxSnapshot)) {
						// bounce volume
						_tempModel = new ForecastModel();
						try {
							currentMaxSnapshot = OrderRateUtil.addTime(currentMaxSnapshot);
							_tempModel.setSnapshotTime(DateUtil.getServerTime(currentMaxSnapshot));
							_tempModel.setVolume(currentMaxBounce);
							responseMessage.getBounce().add(_tempModel);
						} catch (ParseException e) {					
							e.printStackTrace();
						}
					}
					
				}
			}
			if(rollList != null) {
				for (ListIterator<RollData> i = rollList.listIterator(); i.hasNext();) {
					RollData _roll = i.next();	
					currentMaxSnapshot = _roll.getSnapshotTime();
					
					if(_roll.getZone() == null
							|| (cutoffMap != null 
									&& cutoffMap.get(_roll.getCutOff()) != null
									&& cutoffMap.get(_roll.getCutOff()).equals(shift))) {
						
						currentMaxRoll = (int) _roll.getCnt();
						
						// roll volume
						_tempModel = new ForecastModel();
						_tempModel.setSnapshotTime(DateUtil.getServerTime(_roll.getSnapshotTime()));
						_tempModel.setVolume((int)_roll.getCnt());
						responseMessage.getRoll().add(_tempModel);
					}
				}
				
				if(currentMaxSnapshot != null) {
					Date maxSnapshot = DateUtil.getMaxSnapshot(currentMaxSnapshot);
					while(currentMaxSnapshot.before(maxSnapshot)) {
						// roll volume
						_tempModel = new ForecastModel();
						try {
							currentMaxSnapshot = OrderRateUtil.addTime(currentMaxSnapshot);
							_tempModel.setSnapshotTime(DateUtil.getServerTime(currentMaxSnapshot));
							_tempModel.setVolume(currentMaxRoll);
							responseMessage.getRoll().add(_tempModel);
						} catch (ParseException e) {					
							e.printStackTrace();
						}
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getForecast: "+ e.getMessage());
			throw new ServiceException(ErrorCodeEventEnum.UNKNOWN_ERROR, 
					"Failed to load event data");
		}
		return responseMessage;
	}
	
	@RequestMapping(value = "/excel", method = RequestMethod.POST)
    public @ResponseBody String excel(String excel, String extension) throws IOException {
        if (extension.equals("csv") || extension.equals("xml")) {

            String filename = "pqGrid." + extension;
            File file = new File(filename);

            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(excel);
            bw.close();
            return filename;
        } else {
            return "";
        }
    }

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public void excel(String filename, HttpServletRequest request, HttpServletResponse response) throws IOException {
       
		File outputFile = new File(filename);
		response.setBufferSize((int) outputFile.length());

		response.setHeader("Content-Disposition", "attachment; filename=\""+outputFile.getName()+"\"");
		response.setContentType("application/x-download");
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=0");
		response.setContentLength((int)outputFile.length());
		FileCopyUtils.copy(new FileInputStream(outputFile), response.getOutputStream());
		response.getOutputStream().close();
    }
    
    @RequestMapping(value = "/exportchart", method = RequestMethod.POST)
    public void exportchart(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String dataUri = request.getParameter("data-uri");
    	if (dataUri != null && !"".equals(dataUri)) {
    		// string off header, and coming from the request param, + is
    		// changed to a space, change it back (otherwise you get Illegal
    		// character in Base64 encoded data)
    		dataUri = dataUri.replace("data:image/png;base64,", "").replace(" ", "+");
    		
    		logger.info("dataUri:" + dataUri);    		
    		InputStream input = new ByteArrayInputStream(Base64Decode.decode(dataUri));
    		response.setContentType ("image/png");
    		response.setHeader ("Content-Disposition", "attachment; filename=\"chart.png\"");
    								
    		BufferedImage img = null;
    		img = ImageIO.read(input);
    		ImageIO.write(img, "png", new File(System.getProperty("user.home") + "/Desktop/chart.png"));
    		response.getOutputStream().close();
    	} else {
    		logger.info("Invalid datauri param.");
    	}
    }

}
