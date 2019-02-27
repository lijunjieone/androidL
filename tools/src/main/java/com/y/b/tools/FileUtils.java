package com.y.b.tools;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.webkit.MimeTypeMap;


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Random;

/**
 * 文件操作相关的类，流相关的操作请到IOUtils中。
 */
public final class FileUtils {
    private static final String LOG_TAG = "FileUtils";

	/**
	 * 删除一个目录 并且会递归遍历删除子目录
	 * TODO 可以优化，主要是不使用递归
	 * @param dir 目录
	 * @return 不存在返回和文件 返回false
	 */
	public static boolean deleteDir(File dir) {
		if (dir == null || !dir.exists() || dir.isFile()) {
			return false;
		}

		File[] dirs = dir.listFiles();
		if(dirs == null) {
			return false;
		}
		for (File file : dirs ) {
			if(file == null) {
				continue;
			}else if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				deleteDir(file);// 递归
			}
		}
		return dir.delete();
	}

	/**
	 * 删除一个文件和空目录
	 * @param filename
	 * @return 删除失败，文件不存在，还有子文件的目录返回false。
	 */
	public static boolean deleteFile(String filename) {
		if (TextUtils.isEmpty(filename)){
			return false;
		}

		File file = new File(filename);
		return file.delete();
	}

	/**
	 * 文件和目录是否存在.
	 * @param fileName
	 * @return 不存在的目录或者文件返回false
	 */
	public static boolean fileExists(String fileName) {
		if(!TextUtils.isEmpty(fileName)) {
			File file = new File(fileName);
			return file.getAbsoluteFile().exists();
		}
		return false;
	}

	/**
	 * 重命名文件 注意：如果新文件存在就先删除。
	 * 同目录重命名文件：成功
	 * 不同目录移动文件: 失败
	 * 重命名空目录：成功
	 * @param oldName 旧的文件名字
	 * @param newName 新的文件名字
	 * @return 重命名失败
	 */
	public static boolean rename(String oldName, String newName){
		if (TextUtils.isEmpty(oldName) || TextUtils.isEmpty(newName)){
			return false;
		}

		File oldFile = new File(oldName);
		File newFile = new File(newName);

		return oldFile.renameTo(newFile);
	}

	/**
	 * 创建目录
	 * @param dirName
	 * @return 存在为false。创建成功返回true。
	 */
	public static boolean createDirectory(String dirName){
		if (TextUtils.isEmpty(dirName)){
			return false;
		}

		File file = new File(dirName);
		if (!file.exists()){
			return file.mkdirs();
		}

		return false;
	}

	/**
	 * 如果文件不存在，则创建新文件。已存在就不在重新创建。
	 * @param path
	 * @return 如果成功创建返回true,已存在或者创建失败返回false。
	 */
	public static boolean createFile(String path) throws IOException {
		if (FileUtils.fileExists(path)){
			return false;
		}

		return createFile(path, false);
	}

    /**
     * 创建新文件。
     * @param path 文件路径
     * @param delWhenExist 文件已存在时是否删除
     * @return 创建失败返回false。 delWhenExist：值为true时，会删除已存在的文件。如果创建成功返回false。
	 * delWhenExist:值未false时。如果文件已存在会返回false。
     */
    public static boolean createFile(String path, boolean delWhenExist) throws IOException {
        if (TextUtils.isEmpty(path)){
            return false;
        }

        boolean flag = false;
        File file = new File(path);
        try {
            if (file.exists()){
				if(delWhenExist){
					file.delete();
				}else{
					return false;
				}
            }

			if (!FileUtils.fileExists(file.getParent())){
				file.getParentFile().mkdirs();
			}

			flag = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

	/**
	 * 保存到文件。文件已存在先删除。
	 * @param content 文件内容 .content不能为null。
	 * @param filename 文件名字
	 * @return 返回false 保存文件失败 返回true 保存文件成功
	 */
	public static boolean saveFile(String content, String filename) {
		if (TextUtils.isEmpty(filename) || content == null) {
			return false;
		}
		return saveFile(content, new File(filename));
	}

	/**
	 * 保存字符串到一个文件中，如果文件已存在先删除
	 * @param content， 不能为null
	 * @param f
	 * @return
	 */
	public static boolean saveFile(String content, File f) {
		if (f == null || content == null){
			return false;
		}
		boolean result = false;
		PrintWriter pw = null;
		try {
			if (!createFile(f.getAbsolutePath(), true)){
				return false;
			}
			pw = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(f))));

			if(pw != null) {
				pw.write(content);
				result = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(pw);
		}

		return result;
	}

	/**
	 * 保存byte数组到文件中
	 * @param data
	 * @param filename
	 * @return
	 */
	public static boolean saveFile(byte[] data, String filename) {
		if (TextUtils.isEmpty(filename) || data == null){
			return false;
		}

		boolean flag = false;
		FileOutputStream out = null;
		try {
			if (createFile(filename, true)){
				out = new FileOutputStream(filename);
				out.write(data);
				out.flush();
				flag = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(out);
		}

		return flag;
	}



	/**
	 * 读取InputStream流中内容到一个文件中
	 * @param in
	 * @param filename 文件名字
	 * @return
	 */
	public static boolean saveFile(InputStream in, String filename) {
		if (TextUtils.isEmpty(filename) || in == null ){
			return false;
		}

		boolean flag = false;
		FileOutputStream out = null;
		try {
            if (!createFile(filename, true)){
                return false;
            }

			out = new FileOutputStream(filename);
			byte[] buf = new byte[1024];
			int len = 0;
			while((len = in.read(buf)) != -1){
				out.write(buf, 0, len);
			}
			flag = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}

		return flag;
	}

	/**
	 * 附加一个字符串到文件中
	 * @param filepath
	 * @param content
	 * @return
	 */
	public static boolean appendStringToFile(String filepath, String content){
		boolean result = false;
		FileOutputStream out = null;
		try {
            createFile(filepath);
			if (!FileUtils.fileExists(filepath)){
				return false;
			}

            File file = new File(filepath);
			out = new FileOutputStream(file, true);
			byte[] data = content.getBytes("UTF-8");
			out.write(data,0,data.length);
			out.close();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
		}

		return result;
	}

	/**
	 * 读取一个文件的内容到byte数组中
	 * @param fileName .不能为null。
	 * @return 如果不存在返回null
	 */
	public static byte[] readFileToBytes(String fileName) {
		if (fileName == null){
			return null;
		}

		File file = new File(fileName);
		return readFileToBytes(file);
	}

	/**
	 * 读取一个文件的内容到byte数组中。file不能为空。
	 * @param file 不能为空
	 * @return 如果不存在返回null
	 */
	public static byte[] readFileToBytes(File file) {
		if (file == null || !file.exists()){
			return null;
		}

		FileInputStream in = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			in = new FileInputStream(file);
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			out.write(buffer);
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		return null;
	}


	/**
	 * 读取文件内容到字符串
	 * @param fileName 不能为null。
	 * @return 如果文件不存在返回未空
	 */
	public static String readFileToString(String fileName) {
		if (fileName == null){
			return null;
		}
		File file = new File(fileName);
		return readFileToString(file);
	}


	/**
	 * 读取文件内容到字符串.
	 * @param file 不能为空，返回范围null。
	 * @return 如果文件不存在返回null
	 */
	public static String readFileToString(File file) {
		byte[] bytes = readFileToBytes(file);
		if (bytes != null){
			return new String(bytes);
		}
		return null;
	}

    /**
     * 拷贝一个文件内容到新文件中，新文件如果不存在就创建,如果存在就先删除在copy。
     * @param srcPath 源文件路径
     * @param targetPath 新文件路径
	 * @return false 移动失败。 true： 移动成功。
     * @throws Exception
     */
    public static boolean copyFile(String srcPath, String targetPath) throws IOException {
        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(targetPath)){
            return false;
        }
        File srcFile = new File(srcPath);
        File targetFile = new File(targetPath);
        return copyFile(srcFile, targetFile);
    }

    /**
     * 拷贝一个文件内容到新文件中，新文件如果不存在就创建，如果存在就先删除在copy。
     * @param src 源文件路径
     * @param target 新文件路径
	 * @return false 移动失败。 true： 移动成功。
     * @throws IOException
     */
    public static boolean copyFile(File src, File target) throws IOException {
        if (src == null || target == null || !src.exists()){
            return false;
        }

        InputStream inStream = null;
		boolean result = false;
        try {
            inStream = new FileInputStream(src);
            result = saveFile(inStream, target.getAbsolutePath());
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }finally {
            IOUtils.closeQuietly(inStream);
        }

        return result;
    }

	/**
	 * 下载文件。必须先获取到InputStream
	 * @param is
	 * @param fileName
	 * @return
	 */
	public static boolean downFile(InputStream is, String fileName){
		if (is == null){
			return false;
		}

		boolean result = false;

		FileOutputStream fileStream = null;
		BufferedInputStream bis = null;
		try {
			if (!FileUtils.createFile(fileName, true)){
				return false;
			}

			File file = new File(fileName);
			fileStream = new FileOutputStream(file);
			bis = new BufferedInputStream(is);

			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = bis.read(buffer)) != -1) {
				fileStream.write(buffer, 0, len);
			}

			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fileStream);
			IOUtils.closeQuietly(bis);
		}
		Log.d(LOG_TAG, "end download url");
		return result;
	}

	/**
	 * 下载一个文件，并保存到给定的文件中
	 * @param url
	 * @param fileName
	 * @return
	 */
	public static boolean downFile(String url, String fileName) {
		Log.d(LOG_TAG, "start download url : " + url);
		if (TextUtils.isEmpty(url) || TextUtils.isEmpty(fileName)) {
			return false;
		}

		InputStream is = HttpHelper.getBodyInputStream(HttpHelper.getResponse(url));
		Log.d(LOG_TAG, "label1");
		if (is == null){
			return false;
		}

		return downFile(is, fileName);
	}

	/**
	 * 生成一个和originPath路径和有效名称相同的文件文件
	 * 比如 /etc/a.txt 生成 /etc/a-1.txt
	 * @param originPath 源文件名称。必须是名称，不能是路径
	 * @param fileNameSeparator 新名称的分割线
	 * @return originPath 为null或者为空，返回null。 fileNameSeparator为null 返回null。
	 * originPath 不存在返回originPath.
	 */
    public static String createUniqueFilename(String originPath, String fileNameSeparator) {
		if (TextUtils.isEmpty(originPath) || fileNameSeparator == null){
			return null;
		}

        if (!new File(originPath).getAbsoluteFile().exists()) {
            return originPath;
        }

		String extension = "";
		String filename = getFileNameFromPath(originPath);
		String path = originPath.substring(0,originPath.length()-filename.length());
		int index = filename.lastIndexOf('.');
		if (index > 0) {
			extension = filename.substring(index);
			filename = filename.substring(0, index);
		}

		filename = filename + fileNameSeparator;
		String newPath=originPath;

		int sequence = 1;
		Random random = new Random(SystemClock.uptimeMillis());
		for (int magnitude = 1; magnitude < 1000000000; magnitude *= 10) {
			for (int iteration = 0; iteration < 9; ++iteration) {
				originPath = filename + sequence + extension;
				newPath=path+originPath;

				if (!fileExists(newPath)) {
					return newPath;
				}
				sequence += random.nextInt(magnitude) + 1;
			}
		}
		return newPath;
    }

	/**
	 * 判断文件是否可写
	 * @param file
	 * @return
	 */
	public static boolean isFileWritable(final String file) {
		if (TextUtils.isEmpty(file)) {
			return false;
		}
		return new File(file).canWrite();
	}

	/**
	 * 从文件路径或者URL路径中获取资源的名字
	 * @param filePath
	 * @return filePath 为null，返回null 。如果 filePath为空 返回空。
     */
	public static String getFileNameFromPath(String filePath){
		if (TextUtils.isEmpty(filePath)){
			return filePath;
		}
		int lastSeparator = filePath.lastIndexOf(File.separator);
		return filePath.substring(lastSeparator+1);
	}

	/**
	 * 获取文件的扩展名。fileName可以为文件名字，文件路径，url路径。
	 * @param pathName
	 * @return 不存在返回为空值。不存在返回null的情况
	 */
    public static String getFileExtension(String pathName) {
		if (TextUtils.isEmpty(pathName)){
			return "";
		}

		String fileName = FileUtils.getFileNameFromPath(pathName);
        int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex <= -1){
			return "";
		}
        return fileName.substring(lastDotIndex + 1).toLowerCase(Locale.getDefault());
    }

	/**
	 * TODO .flv格式的文件暂无法获取其MimeType.
	 * 获取一个文件的MimeType类型
	 * @param filename
	 * @return 如果失败返回null。没有扩展名也返回null。
	 */
	public static String getFileMimeType(String filename) {
		if (TextUtils.isEmpty(filename)) {
			return null;
		}

        String extension = getFileExtension(filename);
		String mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

		if (mimetype != null) {
			if (mimetype.equalsIgnoreCase("text/plain")
					|| mimetype.equalsIgnoreCase("application/octet-stream")) {
				if (filename.contains(".")) {

					if (filename.endsWith("flv")) {
						mimetype = "video/x-flv";
					}
				}
			}
		}
		return mimetype;
	}

	/**
	 * 删除一个文件的内容，但是不删除文件。
	 * @param fileName
	 */
	public static void clearFileContent(String fileName) {
		if (TextUtils.isEmpty(fileName)){
			return;
		}

		saveFile("", fileName);
	}

	/**
	 * 获取目录的大小，单位为KB
	 * @param file
	 * @return
	 */
	public static long getDirSize(File file) {
		long size = 0;
		//判断文件是否存在
		if (file.exists()) {
			//如果是目录则递归计算其内容的总大小
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				for (File f : children)
					size += getDirSize(f);
				return size;
			} else {//如果是文件则直接返回其大小,以“兆”为单位
				size = file.length();
				return size;
			}
		} else {
			return 0;
		}
	}

	/**
	 * 格式化文件的大小。默认调用formatFileSize(Context context, long sizeBytes);
	 * @param c
	 * @param size 单位为字节。
	 * @return
	 */
	public static CharSequence formatFileSize(Context c, long size) {
		return Formatter.formatFileSize(c, size);
	}

	/**
	 * 从Assets读取文件转换为字符串
	 * @param context
	 * @param fileName 文件名字
	 * @return
	 */
	public static String getAssertFileToString(Context context, String fileName){
		if (context == null || TextUtils.isEmpty(fileName)){
			return null;
		}
		String str = "";
		try {
			str = IOUtils.toString(context.getAssets().open(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * 从Assets读取文件转换为byte数组
	 * @param context
	 * @param fileName 文件名字
	 * @return
	 */
	public static byte[] getAssertFileToBytes(Context context, String fileName){
		if (context == null || TextUtils.isEmpty(fileName)){
			return null;
		}
		try {
			return  IOUtils.toByteArray(context.getAssets().open(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
    /**
     * 从Assert中拷贝一个文件内容到新文件中。
     * @param context
     * @param assetFile
     * @param desFile
     * @return
     */
	public static boolean copyAssetFile(Context context, String assetFile, String desFile){
		if (context == null || TextUtils.isEmpty(desFile)){
			return false;
		}
		try {
			return saveFile(context.getAssets().open(assetFile), desFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static InputStream File2ByteArrayInputStream(String filePath) {
		try {
			byte[] data = readStream(new FileInputStream(filePath));
			if (data != null) {
				return new ByteArrayInputStream(data);
			} else {
				return new ByteArrayInputStream(new byte[] {});
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return new ByteArrayInputStream(new byte[] {});
		} catch (Exception e) {
			e.printStackTrace();
			return new ByteArrayInputStream(new byte[] {});
		}
	}

	/**
	 * ＠param 将文件内容解析成字节数组 ＠param inStream ＠return byte[] ＠throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		try {
			byte[] buffer = new byte[1024];
			int len = -1;
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			byte[] data = outStream.toByteArray();
			outStream.close();
			inStream.close();
			return data;
		} catch (Error e) {
			return null;
		}
	}
}
