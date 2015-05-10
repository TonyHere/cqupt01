package com.Tony.server.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
/**
 * 工具类：
 * 用于对象和字节之间的相互转换
 * 并且将两个方法都声明为静态的方便调用
 * @author tony
 *
 */
public class Translate{

	//将对象序列化
	public static byte[] ObjectToByte(java.lang.Object obj) {
		byte[] bytes=new byte[1024];
		try {
			// object to bytearray
			//建立一个比特数组输出流
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			//创建一个写入到bo的ObjectOutputStream
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			//将obj写入到ObjectOutputStream中，而ObjectOutputStream又是将数据
			//写入到ByteArrayOutputStream中的
			oo.writeObject(obj);
			//获取bo中的数据，并赋给byte数组，从而实现了一个对象的序列化
			bytes = bo.toByteArray();
			//bo的关闭无效，关闭后bo中的方法仍然可以被调用
			bo.close();
			oo.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return (bytes);
	}
	//把对象反序列化
	public static java.lang.Object ByteToObject(byte[] bytes) {
		java.lang.Object obj=new java.lang.Object();
		try {
			// bytearray to object
			//创建一个将从指定byte数组读取数据的byte数组输入流
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			//创建一个从指定Byte数组输入流读取数据的ObjectInputStreamObjectInputStream
			ObjectInputStream oi = new ObjectInputStream(bi);
			//将
			obj = oi.readObject();
			//bi关闭无效
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
