package com.cips.constants;

import java.util.HashMap;
import java.util.Map;


public class EnumConstants {

	/**
	 * 审核状态
	 */
	public static enum OrderStsEnum {

		已提交("已提交","0"),
		处理中("处理中","1"),
		已完成("已完成","2"),
		作废删除("作废删除","3");

		/**静态map*/
		private static final Map<String,OrderStsEnum> CODE_MAP = new HashMap<String,OrderStsEnum>();
		static{
			for(OrderStsEnum chnl:OrderStsEnum.values()){
				CODE_MAP.put(chnl.getCode(), chnl);
			}
		}
		
		/**根据编号获取名称*/
		public static String getNameByCode(String code){
			OrderStsEnum chnl = OrderStsEnum.CODE_MAP.get(code);
			return chnl==null?"":chnl.getNamei();
		}

		/**名称*/
		private String namei;
		
		/**编号*/
		private String code;
		
		/**私有构造器*/
		private OrderStsEnum(String namei,String code){
			this.namei = namei;
			this.code = code;
		}

		public String getNamei() {
			return namei;
		}
		
		public void setNamei(String namei) {
			this.namei = namei;
		}
		
		public String getCode() {
			return code;
		}
		
		public void setCode(String code) {
			this.code = code;
		}
		
	}
	}

