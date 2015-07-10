package com.szy.weixin.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import com.szy.weixin.util.WeiXinUtils;
import com.szy.weixin.util.WeiXinUtils.CallBack;

@WebServlet(urlPatterns={"/howOldServlet"})
@MultipartConfig
//@MultipartConfig(maxFileSize=3145728, maxRequestSize=3145728)
public class HowOldServlet extends HttpServlet{

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		
		List<Map<String, String>> resultList = null;
		String imgName = null;
		
		String url = request.getParameter("url-field");
		Part file = null;
		if(url != null){
			int index = url.lastIndexOf("/");
			imgName = url.substring(index+1);    
			resultList = WeiXinUtils.getResultList(url,null);
		}else{
			file = request.getPart("post-field");
			imgName = file.getSubmittedFileName();
			resultList = WeiXinUtils.getResultList(null,file);
		}
		
		if(resultList != null && !resultList.get(0).containsKey("error")){  
			BufferedImage img = null;
			try {
				img = drawImage(resultList,request, url, file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			BufferedImage resultImg = resizeResult(400, 450, img);
			
			File tofile = new File("Y:/weixin/face/"+imgName);   
			request.setAttribute("imgName", imgName);
			
			
			out.print(imgName); 
			ImageIO.write(resultImg, "png", new FileOutputStream(tofile));
			
//			ImageIO.write(resultImg, "png", response.getOutputStream());
		}
	}      

	//������ʾ��
	public BufferedImage drawImage(List<Map<String,String>> resultList,HttpServletRequest request,String url, Part file) throws Exception{
		Map<String,String> mapResult = resultList.get(0);
		BufferedImage img = null;
		if(url != null){
			URL urlImg = new URL(url);
			img = ImageIO.read(urlImg);
		}else{
			img = ImageIO.read(file.getInputStream());
		}

		Graphics2D g = (Graphics2D)img.getGraphics();
			
			//���߿���������Χ===========
			int imageWidth = Integer.parseInt(mapResult.get("imageWidth"));
			int imageHeight = Integer.parseInt(mapResult.get("imageHeight"));
			int center_x = (int) (Float.parseFloat(mapResult.get("center_x"))*imageWidth/100);
			int center_y = (int) (Float.parseFloat(mapResult.get("center_y"))*imageHeight/100);
			int faceWidth = (int) (Float.parseFloat(mapResult.get("faceWidth"))*imageWidth/100);
			int faceHeight = (int) (Float.parseFloat(mapResult.get("faceHeight"))*imageHeight/100);
			
			int x = center_x-faceWidth/2; //x yΪ�������ε����Ͻ�����
			int y = center_y-faceHeight/2;
			g.setColor(Color.WHITE);
			g.drawRect(x,y,faceWidth,faceHeight);	
			
			//���Ա�����:��һ�����ݿ�=========
			String gender = mapResult.get("gender");
			String age = mapResult.get("age");
			
			File hint_file = new File(request.getServletContext().getRealPath("/image/hint.png"));
			File female_file = new File(request.getServletContext().getRealPath("/image/female.png"));
			File male_file = new File(request.getServletContext().getRealPath("/image/male.png"));
			
			InputStream hint_is = new FileInputStream(hint_file);
			InputStream female_is = new FileInputStream(female_file);
			InputStream male_is = new FileInputStream(male_file);
			//ԭͼ��
			BufferedImage hint_img = ImageIO.read(hint_is);
			BufferedImage female_img = ImageIO.read(female_is);
			BufferedImage male_img = ImageIO.read(male_is);
			
			//�Ŵ���С���ͼ��
			BufferedImage rhint_img =  resizeHint(faceWidth,hint_img);
			BufferedImage rfemale_img =  resizeGender(rhint_img,female_img);
			BufferedImage rmale_img = resizeGender(rhint_img,male_img);
		
			int x1 = center_x - rhint_img.getWidth()/2;
			int y1 = center_y - faceHeight/2 - rhint_img.getHeight();
			Graphics2D rhint_g = (Graphics2D)rhint_img.getGraphics();
			//��age����hintͼ����
			rhint_g.setColor(Color.RED);
			rhint_g.setFont(new Font("����", Font.BOLD, 30));
			rhint_g.drawString(age, rhint_img.getWidth()*8/15, rhint_img.getHeight()/2);
			if(gender.equals("Female")){
				//���Ա�ͼ����hintͼ����
				rhint_g.drawImage(rfemale_img, rhint_img.getWidth()/5, rhint_img.getHeight()/10, null);
				//��hintͼ�껭��ԭͼ��   
				g.drawImage(rhint_img, x1, y1, null);
			}else{ 
				rhint_g.drawImage(rmale_img, rhint_img.getWidth()/5, rhint_img.getHeight()/10, null);
				g.drawImage(rhint_img, x1, y1, null);
			}
		  
		return img;		
	}
	
	
	//�Ŵ���Сhintͼ��
	public static BufferedImage resizeHint(int faceWidth,BufferedImage srcImg){
		int imgWidth = 0;
		if(faceWidth>=60 && faceWidth<120){
			imgWidth = faceWidth*7/5;
		}else if(faceWidth < 60){
			imgWidth = faceWidth*2;
		}else{
			imgWidth = faceWidth*8/9;
		}
		int imgHeight = imgWidth*2/3;
		
		//�����µ�ͼƬ
		BufferedImage resizedImg = new BufferedImage(imgWidth,imgHeight,BufferedImage.TYPE_INT_RGB); 
		//��ԭͼ�Ŵ����С������:���ұ���pngͼƬ�Ŵ����С�󱳾�ɫ��͸���Ķ����Ǻ�ɫ
		Graphics2D resizedG = resizedImg.createGraphics(); 
		resizedImg = resizedG.getDeviceConfiguration().createCompatibleImage(imgWidth,imgHeight,Transparency.TRANSLUCENT); 
		resizedG.dispose(); 
		resizedG = resizedImg.createGraphics(); 
		Image from = srcImg.getScaledInstance(imgWidth, imgHeight, srcImg.SCALE_AREA_AVERAGING); 
		resizedG.drawImage(from, 0, 0, null);
		resizedG.dispose();           
          
		return resizedImg;
	}
	
	//�Ŵ���С�Ա�ͼ��
	public BufferedImage resizeGender(BufferedImage rhintImg, BufferedImage srcImg){
		
		int imgWidth = rhintImg.getWidth()/3;
		int imgHeight = rhintImg.getHeight()*2/3;
		
		//�����µ�ͼƬ
		BufferedImage resizedImg = new BufferedImage(imgWidth,imgHeight,BufferedImage.TYPE_INT_RGB); 
		//��ԭͼ�Ŵ����С������:���ұ���pngͼƬ�Ŵ����С�󱳾�ɫ��͸���Ķ����Ǻ�ɫ
		Graphics2D resizedG = resizedImg.createGraphics(); 
		resizedImg = resizedG.getDeviceConfiguration().createCompatibleImage(imgWidth,imgHeight,Transparency.TRANSLUCENT); 
		resizedG.dispose(); 
		resizedG = resizedImg.createGraphics(); 
		Image from = srcImg.getScaledInstance(imgWidth, imgHeight, srcImg.SCALE_AREA_AVERAGING); 
		resizedG.drawImage(from, 0, 0, null);
		resizedG.dispose();    
		
		return resizedImg;
	}
	
	//�������ͼ,ͳһͼƬ��С
	public BufferedImage resizeResult(int imgWidth, int imgHeight, BufferedImage srcImg){
		BufferedImage resultImg = new BufferedImage(imgWidth,imgHeight,BufferedImage.TYPE_INT_RGB);
		Graphics2D resizedG = resultImg.createGraphics(); 
		resultImg = resizedG.getDeviceConfiguration().createCompatibleImage(450,380,Transparency.TRANSLUCENT); 
		resizedG.dispose(); 
		resizedG = resultImg.createGraphics(); 
		Image from = srcImg.getScaledInstance(450, 380, srcImg.SCALE_AREA_AVERAGING); 
		resizedG.drawImage(from, 0, 0, null);
		resizedG.dispose();     
		
		return resultImg;
	}
	
	
}
