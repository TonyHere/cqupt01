package com.Tony.server.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
/**
 * �����ࣺ
 * ���ڶ�����ֽ�֮����໥ת��
 * ���ҽ���������������Ϊ��̬�ķ������
 * @author tony
 *
 */
public class Translate{

	//���������л�
	public static byte[] ObjectToByte(java.lang.Object obj) {
		byte[] bytes=new byte[1024];
		try {
			// object to bytearray
			//����һ���������������
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			//����һ��д�뵽bo��ObjectOutputStream
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			//��objд�뵽ObjectOutputStream�У���ObjectOutputStream���ǽ�����
			//д�뵽ByteArrayOutputStream�е�
			oo.writeObject(obj);
			//��ȡbo�е����ݣ�������byte���飬�Ӷ�ʵ����һ����������л�
			bytes = bo.toByteArray();
			//bo�Ĺر���Ч���رպ�bo�еķ�����Ȼ���Ա�����
			bo.close();
			oo.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return (bytes);
	}
	//�Ѷ������л�
	public static java.lang.Object ByteToObject(byte[] bytes) {
		java.lang.Object obj=new java.lang.Object();
		try {
			// bytearray to object
			//����һ������ָ��byte�����ȡ���ݵ�byte����������
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			//����һ����ָ��Byte������������ȡ���ݵ�ObjectInputStreamObjectInputStream
			ObjectInputStream oi = new ObjectInputStream(bi);
			//��
			obj = oi.readObject();
			//bi�ر���Ч
			bi.close();
			oi.close();
		} catch (Exception e) {
			System.out.println("myClient_translation: " + e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}
	
	public Translate(){
		
	}
}
