package com.yipeng.libary.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class PhotoUtils {

	public final static String IMAGE_SUFFIX = ".jpg";

	// 相册的RequestCode
	public static final int INTENT_REQUEST_CODE_ALBUM = 0;
	
	// 照相的RequestCode
	public static final int INTENT_REQUEST_CODE_CAMERA = 1;

	public static final int REQUEST_CODE_TAKE_PICTURE = 12;
	
	// 裁剪照片的RequestCode
	public static final int INTENT_REQUEST_CODE_CROP = 2;
 
	// 滤镜图片的RequestCode
	public static final int INTENT_REQUEST_CODE_FLITER = 3;

	/**
	 * 通过手机相册获取图片
	 * 
	 * @param activity
	 */
	public static void selectPhoto(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_ALBUM);
	}

	/**
	 * 通过手机照相获取图片
	 * 
	 * @param activity
	 * @return 照相后图片的路径
	 */
	public static String takePicture(Activity activity) {
		
		String folderpath = FileUtils.getPath(FileUtils.FolderType.IMAGE);
		FileUtils.createDirFile(folderpath);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String path = folderpath + UUID.randomUUID().toString() + ".jpg";
		File file = FileUtils.createNewFile(path);
		if (file != null) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		}
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CAMERA);
		return path;
	}

	public static Bitmap autoCropPhoto(Bitmap data) {
		// 转换成120宽与高的图片
		Bitmap transBitmap = createBitmap(data, 120, 120);
		return transBitmap;
	}

	/**
	 * 删除图片缓存目录
	 */
	public static void deleteImageFile() {
		String folderpath = FileUtils.getPath(FileUtils.FolderType.IMAGE);
		File dir = new File(folderpath);
		if (dir.exists()) {
			FileUtils.delFolder(folderpath);
		}
	}

	public static String copyImageFile(String sourcePath, String fileName) {
		if (!FileUtils.isSdcardExist()) {
			return null;
		}
		FileOutputStream fileOutputStream = null;
		FileInputStream fileInputStream = null;
		String folderpath = FileUtils.getPath(FileUtils.FolderType.IMAGE);
		FileUtils.createDirFile(folderpath);
		
		File sourceFile = new File(sourcePath);
		if (!sourceFile.exists()) {
			return null;
		}

		String newFilePath = folderpath + fileName;
		File file = FileUtils.createNewFile(newFilePath);
		if (file == null) {
			return null;
		}
		try {
			fileOutputStream = new FileOutputStream(newFilePath);
			fileInputStream = new FileInputStream(sourceFile);
			byte[] buffer = new byte[1024];
			int byteread = 0;
			while ((byteread = fileInputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer);
			}
		} catch (FileNotFoundException e1) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileInputStream.close();
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (IOException e) {
				return null;
			}
		}
		return newFilePath;
	}

	public static boolean copyFile(String sourcePath, String targetPath) {
		if (!FileUtils.isSdcardExist()) {
			return false;
		}
		FileOutputStream fileOutputStream = null;
		FileInputStream fileInputStream = null;
		
		String folderpath = FileUtils.getPath(FileUtils.FolderType.IMAGE);
		FileUtils.createDirFile(folderpath);

		File sourceFile = new File(sourcePath);
		if (!sourceFile.exists()) {
			return false;
		}

		FileUtils.createDirFile(folderpath);

		String newFilePath = targetPath;// IMAGE_PATH + fileName;
		File file = FileUtils.createNewFile(newFilePath);
		if (file == null) {
			return false;
		}
		try {
			fileOutputStream = new FileOutputStream(newFilePath);
			fileInputStream = new FileInputStream(sourceFile);
			byte[] buffer = new byte[1024];
			int byteread = 0;
			while ((byteread = fileInputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer);
			}
		} catch (FileNotFoundException e1) {
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileInputStream.close();
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	
	
	/**
	 * 从文件中获取图片
	 * 
	 * @param path
	 *            图片的路径
	 * @return
	 */
	public static Bitmap getBitmapFromFile(String path) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.RGB_565;
		return BitmapFactory.decodeFile(path, opt);
	}

	/**
	 * set image src
	 * 
	 * @param imageView
	 * @param imagePath
	 */
	public static Bitmap getImageViewBitmapFromFile(String imagePath) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = getImageScale(imagePath);
		Bitmap bm = BitmapFactory.decodeFile(imagePath, option);
		return bm;
	}

	public static Bitmap getImageViewByteFromFile(byte[] data) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = getImageScale(data);
		Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, option);
		return bm;
	}

	public static Bitmap getBitmapBoundsFromFile(String path) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		return BitmapFactory.decodeFile(path, opt);
	}

	public final static int IMAGE_MAX_WIDTH = 540;
	public final static int IMAGE_MAX_HEIGHT = 960;

	/**
	 * scale image to fixed height and weight
	 * 
	 * @param imagePath
	 * @return
	 */
	private static int getImageScale(String imagePath) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		// set inJustDecodeBounds to true, allowing the caller to query the
		// bitmap info without having to allocate the
		// memory for its pixels.
		option.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, option);

		int scale = 1;
		while (option.outWidth / scale >= IMAGE_MAX_WIDTH || option.outHeight / scale >= IMAGE_MAX_HEIGHT) {
			scale *= 2;
		}
		return scale;
	}

	/**
	 * scale image to fixed height and weight
	 * 
	 * @param imagePath
	 * @return
	 */
	private static int getImageScale(byte[] data) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		// set inJustDecodeBounds to true, allowing the caller to query the
		// bitmap info without having to allocate the
		// memory for its pixels.
		option.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, option);

		int scale = 1;
		while (option.outWidth / scale >= IMAGE_MAX_WIDTH || option.outHeight / scale >= IMAGE_MAX_HEIGHT) {
			scale *= 2;
		}
		return scale;
	}

	/**
	 * 从Uri中获取图片
	 * 
	 * @param cr
	 *            ContentResolver对象
	 * @param uri
	 *            图片的Uri
	 * @return
	 */
	public static Bitmap getBitmapFromUri(ContentResolver cr, Uri uri) {
		try {
			return BitmapFactory.decodeStream(cr.openInputStream(uri));
		} catch (FileNotFoundException e) {

		}
		return null;
	}

	/**
	 * 根据宽度和长度进行缩放图片
	 * 
	 * @param path
	 *            图片的路径
	 * @param w
	 *            宽度
	 * @param h
	 *            长度
	 * @return
	 */
	public static Bitmap createBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据宽度和长度进行缩放图片
	 * 
	 * @param path
	 *            图片的路径
	 * @param w
	 *            宽度
	 * @param h
	 *            长度
	 * @return
	 */
	public static Bitmap createBitmap(Bitmap bitmap, int w, int h) {
		try {
			int srcWidth = bitmap.getWidth();// 获取图片的原始宽度
			int srcHeight = bitmap.getHeight();// 获取图片原始高度

			Matrix matrix = new Matrix();
			// 缩放的比例
			float ratio = (float) 120 / Math.min(srcWidth, srcHeight);

			matrix.setScale(ratio, ratio);
			int cropX = 0, cropY = 0, cropW = 0, cropH = 0;

			if (srcWidth > srcHeight) {
				cropY = 0;
				cropX = (srcWidth - srcHeight) / 2;
				cropW = srcHeight;
				cropH = srcHeight;
			} else {
				cropX = 0;
				cropY = (srcHeight - srcWidth) / 2;
				cropW = srcWidth;
				cropH = srcWidth;
			}

			// cropX, cropY,cropW,cropH,
			// 获取缩放后图片
			Bitmap bm = Bitmap.createBitmap(bitmap, cropX, cropY, cropW, cropH, matrix, false);

			return bm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public static Bitmap compressImage(String path) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();

		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bit = BitmapFactory.decodeFile(path, newOpts);

		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}

		if (be <= 0) {
			be = 1;
		}

		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		newOpts.inJustDecodeBounds = false;
		bit = BitmapFactory.decodeFile(path, newOpts);
		return compressBitmap(bit);
	}

	public static Bitmap compressBitmap(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
//		showToastMsg((baos.toByteArray().length/1024)+"KB");
		
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	/**
	 * 获取图片的长度和宽度
	 * 
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static Bundle getBitmapWidthAndHeight(Bitmap bitmap) {
		Bundle bundle = null;
		if (bitmap != null) {
			bundle = new Bundle();
			bundle.putInt("width", bitmap.getWidth());
			bundle.putInt("height", bitmap.getHeight());
			return bundle;
		}
		return null;
	}

	/**
	 * 判断图片高度和宽度是否过大
	 * 
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static boolean bitmapIsLarge(Bitmap bitmap) {
		// final int MAX_WIDTH = 60;
		// final int MAX_HEIGHT = 60;
		Bundle bundle = getBitmapWidthAndHeight(bitmap);
		if (bundle != null) {
			int width = bundle.getInt("width");
			int height = bundle.getInt("height");
			if (width > IMAGE_MAX_WIDTH || height > IMAGE_MAX_HEIGHT) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断图片高度和宽度是否过大
	 * 
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static boolean bitmapIsLarge(byte[] data) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, newOpts);

		Bundle bundle = getBitmapWidthAndHeight(bitmap);
		if (bundle != null) {
			int width = bundle.getInt("width");
			int height = bundle.getInt("height");
			if (width > IMAGE_MAX_WIDTH || height > IMAGE_MAX_HEIGHT) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据比例缩放图片
	 * 
	 * @param screenWidth
	 *            手机屏幕的宽度
	 * @param filePath
	 *            图片的路径
	 * @param ratio
	 *            缩放比例
	 * @return
	 */
	public static Bitmap compressionPhoto(float screenWidth, String filePath, int ratio) {
		Bitmap bitmap = PhotoUtils.getBitmapFromFile(filePath);
		Bitmap compressionBitmap = null;
		float scaleWidth = screenWidth / (bitmap.getWidth() * ratio);
		float scaleHeight = screenWidth / (bitmap.getHeight() * ratio);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		try {
			compressionBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		} catch (Exception e) {
			return bitmap;
		}
		return compressionBitmap;
	}

	/**
	 * 根据比例缩放图片
	 * 
	 * @param screenWidth
	 *            手机屏幕的宽度
	 * @param filePath
	 *            图片的路径
	 * @param ratio
	 *            缩放比例
	 * @return
	 */
	public static Bitmap compressionPhoto(String filePath, float rate) {
		Bitmap bitmap = PhotoUtils.getImageViewBitmapFromFile(filePath);
		int witdh = bitmap.getWidth();
		int height = bitmap.getHeight();

		if (Math.max(witdh, height) > 1024) {
			float scale = rate / Math.max(witdh, height);
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);
			try {
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			} catch (Exception e) {
				return bitmap;
			}
		}

		return bitmap;

	}

	public static Bitmap compressionPhoto(Bitmap bitmap, float rate) {
		if (bitmap == null) {
			return null;
		}
		int witdh = bitmap.getWidth();
		int height = bitmap.getHeight();

		if (Math.max(witdh, height) > 1024) {
			float scale = rate / Math.max(witdh, height);
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);
			try {
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			} catch (Exception e) {
				return bitmap;
			}
		}

		return bitmap;

	}

	/**
	 * 保存图片到SD卡
	 * 
	 * @param bitmap
	 *            图片的bitmap对象
	 * @param bitmap
	 *            文件名
	 * @return
	 */
	public static String savePhotoToSDCard(Bitmap bitmap, String fileName) {
		if (!FileUtils.isSdcardExist()) {
			return null;
		}
		FileOutputStream fileOutputStream = null;
		
		String folderpath = FileUtils.getPath(FileUtils.FolderType.IMAGE);
		FileUtils.createDirFile(folderpath);

		String newFilePath = folderpath + fileName + PhotoUtils.IMAGE_SUFFIX;
		File file = FileUtils.createNewFile(newFilePath);
		if (file == null) {
			return null;
		}
		try {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			fileOutputStream = new FileOutputStream(newFilePath);
			int options = 100;
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

			int size = baos.size();
			int kb = size / 1024;
			// 200k
			if (kb > 200) {
				options = (int) (((float) 200 / kb) * 100);
				if (options < 10) {
					options = 10;
				}
			}

			while ((size / 1024) > 200 && options > 5) {
				baos.reset();
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				options -= 10;
				size = baos.size();
			}
			baos.writeTo(fileOutputStream);
			baos.close();

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			newFilePath = null;
		} catch (IOException e) {
			e.printStackTrace();
			newFilePath = null;
		} catch (Exception e2) {
			e2.printStackTrace();
			newFilePath = null;

		} finally {
			try {
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (IOException e) {
				return null;
			}
		}
		return newFilePath;
	}

	 

	/**
	 * 滤镜效果--LOMO
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap lomoFilter(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int dst[] = new int[width * height];
		bitmap.getPixels(dst, 0, width, 0, 0, width, height);

		int ratio = width > height ? height * 32768 / width : width * 32768 / height;
		int cx = width >> 1;
		int cy = height >> 1;
		int max = cx * cx + cy * cy;
		int min = (int) (max * (1 - 0.8f));
		int diff = max - min;

		int ri, gi, bi;
		int dx, dy, distSq, v;

		int R, G, B;

		int value;
		int pos, pixColor;
		int newR, newG, newB;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pos = y * width + x;
				pixColor = dst[pos];
				R = Color.red(pixColor);
				G = Color.green(pixColor);
				B = Color.blue(pixColor);

				value = R < 128 ? R : 256 - R;
				newR = (value * value * value) / 64 / 256;
				newR = (R < 128 ? newR : 255 - newR);

				value = G < 128 ? G : 256 - G;
				newG = (value * value) / 128;
				newG = (G < 128 ? newG : 255 - newG);

				newB = B / 2 + 0x25;

				// ==========边缘黑暗==============//
				dx = cx - x;
				dy = cy - y;
				if (width > height)
					dx = (dx * ratio) >> 15;
				else
					dy = (dy * ratio) >> 15;

				distSq = dx * dx + dy * dy;
				if (distSq > min) {
					v = ((max - distSq) << 8) / diff;
					v *= v;

					ri = (int) (newR * v) >> 16;
					gi = (int) (newG * v) >> 16;
					bi = (int) (newB * v) >> 16;

					newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
					newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
					newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
				}
				// ==========边缘黑暗end==============//

				dst[pos] = Color.rgb(newR, newG, newB);
			}
		}

		Bitmap acrossFlushBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
		acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
		return acrossFlushBitmap;
	}


	/**
	 * @return 返回指定笔和指定字符串的长度
	 */
	public static float getFontlength(Paint paint, String str) {
		return paint.measureText(str);
	}

	/**
	 * @return 返回指定笔的文字高度
	 */
	public static float getFontHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.descent - fm.ascent;
	}

	/**
	 * @return 返回指定笔离文字顶部的基准距离
	 */
	public static float getFontLeading(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.leading - fm.ascent;
	}

	/**
	 * 获取圆角图片
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 获取颜色的圆角bitmap
	 * 
	 * @param context
	 * @param color
	 * @return
	 */
	public static Bitmap getRoundBitmap(Context context, int color) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.0f, metrics));
		int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4.0f, metrics));
		int round = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.0f, metrics));
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);
		canvas.drawRoundRect(new RectF(0.0F, 0.0F, width, height), round, round, paint);
		return bitmap;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}



}
