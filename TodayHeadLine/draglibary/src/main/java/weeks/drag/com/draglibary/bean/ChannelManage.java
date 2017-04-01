package weeks.drag.com.draglibary.bean;

import android.database.SQLException;
import android.util.Log;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import weeks.drag.com.draglibary.dao.ChannelDao;
import weeks.drag.com.draglibary.db.SQLHelper;


public class ChannelManage {
	public static ChannelManage channelManage;

	public static List<ChannelItem> defaultUserChannels;

	public static List<ChannelItem> defaultOtherChannels;
	private ChannelDao channelDao;

	private boolean userExist = false;
	static {
		defaultUserChannels = new ArrayList<ChannelItem>();
		defaultOtherChannels = new ArrayList<ChannelItem>();
		defaultUserChannels.add(new ChannelItem(1, "新闻", 1, 1));
		defaultUserChannels.add(new ChannelItem(2, "热点", 2, 1));
		/*defaultUserChannels.add(new ChannelItem(3, "国际", 3, 1));
		defaultUserChannels.add(new ChannelItem(4, "国内", 4, 1));
		defaultUserChannels.add(new ChannelItem(5, "头条", 5, 1));
		defaultUserChannels.add(new ChannelItem(6, "社会", 6, 1));
		defaultUserChannels.add(new ChannelItem(7, "娱乐", 7, 1));*/
		defaultOtherChannels.add(new ChannelItem(8, "体育", 1, 0));
		defaultOtherChannels.add(new ChannelItem(9, "军事", 2, 0));
	/*	defaultOtherChannels.add(new ChannelItem(10, "科技", 3, 0));
		defaultOtherChannels.add(new ChannelItem(11, "财经", 4, 0));
		defaultOtherChannels.add(new ChannelItem(12, "时尚", 5, 0));
		defaultOtherChannels.add(new ChannelItem(13, "视频", 6, 0));
		defaultOtherChannels.add(new ChannelItem(14, "养生", 7, 0));
		defaultOtherChannels.add(new ChannelItem(15, "美女", 8, 0));
		defaultOtherChannels.add(new ChannelItem(16, "美食", 9, 0));
		defaultOtherChannels.add(new ChannelItem(17, "汽车", 10, 0));
*/
	}
	private ChannelItem item_c;
	private ChannelItem itemo_c;
	public ChannelManage(ChannelItem item,ChannelItem itemo) {
		this.item_c=item;
		this.itemo_c=itemo;
		defaultUserChannels.add(item_c);
		defaultOtherChannels.add(itemo_c);
	}

	private ChannelManage(SQLHelper paramDBHelper) throws SQLException {
		if (channelDao == null)
			channelDao = new ChannelDao(paramDBHelper.getContext());
		// NavigateItemDao(paramDBHelper.getDao(NavigateItem.class));
		return;
	}

	public static ChannelManage getManage(SQLHelper dbHelper)throws SQLException {
		if (channelManage == null)
			channelManage = new ChannelManage(dbHelper);
		return channelManage;
	}


	public void deleteAllChannel() {
		channelDao.clearFeedTable();
	}


	public List<ChannelItem> getUserChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?",new String[] { "1" });
		if (cacheList != null && !((List) cacheList).isEmpty()) {
			userExist = true;
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			List<ChannelItem> list = new ArrayList<ChannelItem>();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate = new ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				list.add(navigate);
			}
			return list;
		}
		initDefaultChannel();
		return defaultUserChannels;
	}
	

	public List<ChannelItem> getOtherChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?" ,new String[] { "0" });
		List<ChannelItem> list = new ArrayList<ChannelItem>();
		if (cacheList != null && !((List) cacheList).isEmpty()){
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate= new ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				list.add(navigate);
			}
			return list;
		}
		if(userExist){
			return list;
		}
		cacheList = defaultOtherChannels;
		return (List<ChannelItem>) cacheList;
	}
	
	/**
	 * �����û�Ƶ�������ݿ�
	 * @param userList
	 */
	public void saveUserChannel(List<ChannelItem> userList) {
		for (int i = 0; i < userList.size(); i++) {
			ChannelItem channelItem = (ChannelItem) userList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(1));
			channelDao.addCache(channelItem);
		}
	}
	
	/**
	 * ��������Ƶ�������ݿ�
	 * @param otherList
	 */
	public void saveOtherChannel(List<ChannelItem> otherList) {
		for (int i = 0; i < otherList.size(); i++) {
			ChannelItem channelItem = (ChannelItem) otherList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(0));
			channelDao.addCache(channelItem);
		}
	}
	
	/**
	 * ��ʼ�����ݿ��ڵ�Ƶ������
	 */
	private void initDefaultChannel(){
		Log.d("deleteAll", "deleteAll");
		deleteAllChannel();
		saveUserChannel(defaultUserChannels);
		saveOtherChannel(defaultOtherChannels);
	}
}
