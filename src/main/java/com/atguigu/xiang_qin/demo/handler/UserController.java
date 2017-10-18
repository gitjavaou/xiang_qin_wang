package com.atguigu.xiang_qin.demo.handler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrServerException;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.atguigu.xiang_qin.demo.pojo.User;
import com.atguigu.xiang_qin.demo.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/demo/user/search")
	public String search(@RequestParam("keywords") String keywords,Model model) throws SolrServerException{

		List<Map<String,String>> list = userService.getDataFromSolrIndex(keywords);
		model.addAttribute("list", list);
		return "user_search";
	}
	
	/**
	 * 鏇存柊鐨勬搷浣滐紝杩炲甫鏈塻olr绛夌瓑
	 * @param user
	 * @param headPicture
	 * @param session
	 * @return
	 * @throws IOException
	 * @throws MyException
	 */
	@RequestMapping("/demo/user/update")
	public String update(User user,@RequestParam("headPicture") MultipartFile headPicture,HttpSession session ) 
			throws IOException, MyException{
		
		//1.鍒ゆ柇headPicture鏄惁涓虹┖
		if (!headPicture.isEmpty()) {
			//2.璋冪敤FastDFS杩滅▼鏈嶅姟鎵ц鏂囦欢涓婁紶
			//鈶�鑾峰彇tracker_server.conf鐨勭墿鐞嗙粷瀵硅矾寰�
			String path = this.getClass().getResource("/tracker_server.conf").getPath();
			
			//鈶�鍏ㄥ眬鍒濆鍖�
			ClientGlobal.init(path);
			
			//鈶�鍒涘缓 4涓璞�
			TrackerClient trackerClient = new TrackerClient();			
			TrackerServer trackerServer = trackerClient.getConnection();
			
			StorageServer storageServer = null;			
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);
			
			//鈶�鎵ц鏂囦欢涓婁紶骞惰幏鍙栬繑鍥炲�
				//1锛塨ytes 涓婁紶鏂囦欢鐨勫瓧鑺傛暟缁�
			byte[] bytes = headPicture.getBytes();
			
				//2锛塮ile_ext_name 鏂囦欢鐨勬嫇灞曞悕
			String originalFilename = headPicture.getOriginalFilename();
			int lastIndexOf = originalFilename.lastIndexOf(".")+1;
			String file_ext_name = originalFilename.substring(lastIndexOf);
			
				//3锛�鏂囦欢鐨勫厓鏁版嵁
			NameValuePair[] meta_list = null;
			
				//4)鎵ц鏂囦欢涓婁紶,涓婁紶鎴愬姛鍚庣敓鎴愮殑鏂囦欢鐨勭粍鍚嶅拰鏂囦欢鍚嶅湪杩斿洖鍊间腑
			String[] results = storageClient.upload_file(bytes, file_ext_name, meta_list);
			
				//5锛夎幏鍙栫粍鍚�
			String userPicGroup = results[0];
			
				//6锛夎幏鍙栨枃浠跺悕
			String userPicFilename =results[1];
			
				//**鑾峰彇鏃х殑缁勫悕鍜屾枃浠跺悕
			String oldGroup = user.getUserPicGroup();
			String oldFileName = user.getUserPicFilename();
			
				//鍒犻櫎鏃ф枃浠�
			if (oldGroup != null && !"".equals(oldGroup)) {
				storageClient.delete_file(oldGroup, oldFileName);
			}
			
				//7锛夋妸缁勫悕鍜屾枃浠跺悕璁剧疆鍒皍ser涓�
			user.setUserPicGroup(userPicGroup);
			user.setUserPicFilename(userPicFilename);
			
		}
		//3.鎵цUser瀵硅薄鐨勬洿鏂版搷浣�
		try {
			userService.updateUserByExample(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//bug:鏇存柊鎿嶄綔瀹屾垚鍚庯紝鏁版嵁搴撲笌椤甸潰鏃犳硶鍚屾
		user = userService.getUserById(user.getUserId());
		session.setAttribute("loginUser", user);
		return "redirect:/index.jsp";
	}
	
	/**
	 * 鐢ㄦ埛閫�嚭鍔熻兘鐨勫疄鐜�
	 * @param session
	 * @return
	 */
	@RequestMapping("/demo/user/logout")
	public String logout(HttpSession session){
		
		session.invalidate();
		
		return "redirect:/index.jsp";
	}
	
	/**
	 * 鐢ㄦ埛鐨勭櫥褰曞疄鐜�
	 * @param userForm
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping("/demo/user/login")
	public String loginUser(User userForm,HttpSession session,Map<String, Object> map){
		//1.浠庢暟鎹簱涓幏鍙栫敤鎴蜂俊鎭�
		User user = userService.getUserForLogin(userForm);
		
		//2.鍒ゆ柇涓嬬敤鎴蜂俊鎭槸鍚︽纭�
		if (user != null) {
			session.setAttribute("loginUser", user);
		}else {
			//鐧诲綍澶辫触,鍒╃敤map鏉ユ惡甯﹀け璐ョ殑杩斿洖淇℃伅
			map.put("message", "鐧诲綍澶辫触锛岀敤鎴峰悕鍜屽瘑鐮侀敊璇紒");
			return "user_login";
		}
		return "redirect:/index.jsp";
	}
	
	/**
	 * 鐢ㄦ埛鐨勬敞鍐屽疄鐜�
	 * @param user
	 * @param map
	 * @return
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@RequestMapping("/demo/user/regist")
	public String registUser(User user,Map<String, Object> map) throws SolrServerException, IOException{
		//1.妫�煡璐﹀彿鏄惁宸茬粡瀛樺湪
		String userName = user.getUserName();
		int count = userService.getUserCountForRegist(userName);
		if (count == 0) {
			//2.璐﹀彿鍙互浣跨敤
			userService.saveUser(user);
			
		}else {
			//3.濡傛灉鐢ㄦ埛鍚嶈鍗犵敤锛屽垯鍥炲埌娉ㄥ唽椤甸潰鏄剧ず閿欒娑堟伅
			map.put("massage", "鐢ㄦ埛鍚嶅凡瀛樺湪锛�");
			return "user_regist";
		}
		return "user_login";
	}
	
}
