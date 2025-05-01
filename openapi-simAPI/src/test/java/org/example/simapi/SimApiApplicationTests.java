package org.example.simapi;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.example.openapiclientsdk.Client;
import org.example.simapi.entity.SongCi;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
class SimApiApplicationTests {
	@Resource
	Client client;

	@Test
	void niocontextLoads() {
		// 177
		// 创建一个 Path 对象，表示要读取的文件路径
		long start = System.currentTimeMillis();
		Path path = Paths.get("src/main/resources/data/songci.json" );
		try {
			// 使用 Files 类的 readAllBytes 方法，将文件的所有字节读取到一个 byte 数组中
			byte[] bytes = Files.readAllBytes(path);
			// 使用 Charset 类的 forName 方法，指定字符编码为 UTF-8，并将 byte 数组转换为字符串
			String json = new String(bytes, Charset.forName("UTF-8"));
			// 打印输出字符串
			JSONArray array = JSONUtil.parseArray(json);
			List<SongCi> songCiList = array.toList(SongCi.class);

			long end = System.currentTimeMillis();
			System.out.println(end - start);
			for (int i = 0; i < 1000; i++) {
				SongCi para = songCiList.get(RandomUtil.randomInt(0,songCiList.size()));
				int size = para.getParagraphs().size();
				System.out.println(para.getRhythmic());
				System.out.println(para.getAuthor());
				System.out.println(para.getParagraphs().get(RandomUtil.randomInt(0,size - 1)));
			}
			/*songCiList.forEach(songCi ->{
						System.out.println(songCiList.toString());
					}
			);*/

		} catch (IOException e) {
			// 处理异常
			e.printStackTrace();
		}
	}
	@Test
	public void niopipetest() {
		// 207
		// 创建一个 Path 对象，表示要读取的文件路径
		long start = System.currentTimeMillis();
		Path path = Paths.get("src/main/resources/data/poet.tang.42000.json");

		try (FileChannel channel = FileChannel.open(
				path,
				StandardOpenOption.READ // 只读模式
		)) {
			// 1. 创建缓冲区并读取文件内容
			ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
			channel.read(buffer);
			buffer.flip(); // 切换到读模式

			byte[] bytes = new byte[buffer.remaining()];
			buffer.get(bytes);
			String json = new String(bytes, StandardCharsets.UTF_8);

			// 2. 解析 JSON 数据
			JSONArray array = JSONUtil.parseArray(json);
			List<SongCi> songCiList = array.toList(SongCi.class);
			long end = System.currentTimeMillis();
			System.out.println(end - start);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String readWithThreads(Path path, int threads) throws IOException, InterruptedException, ExecutionException {
		long fileSize = Files.size(path);
		if (fileSize == 0) return "";

		ExecutorService executor = Executors.newFixedThreadPool(threads);
		List<Future<byte[]>> futures = new ArrayList<>();

		long chunkSize = fileSize / threads;
		long remainder = fileSize % threads;

		for (int i = 0; i < threads; i++) {
			long start = i * chunkSize + Math.min(i, remainder) * 1;
			long length = (i < remainder) ? chunkSize + 1 : chunkSize;

			Callable<byte[]> task = () -> {
				try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
					ByteBuffer buffer = ByteBuffer.allocate((int) length);
					channel.position(start);
					channel.read(buffer);
					buffer.flip();
					byte[] data = new byte[buffer.remaining()];
					buffer.get(data);
					return data;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			};

			futures.add(executor.submit(task));
		}

		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

		byte[] allBytes = new byte[(int) fileSize];
		int offset = 0;
		for (Future<byte[]> future : futures) {
			byte[] chunk = future.get();
			System.arraycopy(chunk, 0, allBytes, offset, chunk.length);
			offset += chunk.length;
		}

		return new String(allBytes, StandardCharsets.UTF_8);
	}
	@Test
	public void ioTest(){
		// 276ms
		long start = System.currentTimeMillis();
		String filePath = "src/main/resources/data/poet.tang.42000.json";
		byte[] bytes = null;

		try {
			// 1. 使用传统IO读取文件到字节数组
			try (FileInputStream fis = new FileInputStream(filePath);
				 BufferedInputStream bis = new BufferedInputStream(fis)) {
				// 计算文件长度（需确保文件不大，否则可能溢出）
				int length = (int) new File(filePath).length();
				bytes = new byte[length];
				int bytesRead = bis.read(bytes, 0, length);
				if (bytesRead != length) {
					throw new IOException("文件读取不完整");
				}
			}

			// 2. 将字节数组转换为字符串（指定UTF-8编码）
			String json = new String(bytes, StandardCharsets.UTF_8);

			// 3. 解析JSON数据（假设JSONUtil和SongCi已定义）
			JSONArray array = JSONUtil.parseArray(json);
			List<SongCi> songCiList = array.toList(SongCi.class);

			long end = System.currentTimeMillis();
			System.out.println("耗时：" + (end - start) + "ms");

			// 4. 随机输出数据（逻辑不变）
            /*for (int i = 0; i < 1000; i++) {
                SongCi para = songCiList.get(
                        ThreadLocalRandom.current().nextInt(songCiList.size())
                );
                int size = para.getParagraphs().size();
                System.out.println(para.getRhythmic());
                System.out.println(para.getAuthor());
                System.out.println(para.getParagraphs().get(
                        ThreadLocalRandom.current().nextInt(size)
                ));
            }*/

		} catch (FileNotFoundException e) {
			System.err.println("文件未找到：" + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void mulnioTest() throws IOException, ExecutionException, InterruptedException {
		// 373ms >...<
		long start = System.currentTimeMillis();
		Path path = Paths.get("src/main/resources/data/poet.tang.42000.json");
		String json = readWithThreads(path,5);
		JSONArray array = JSONUtil.parseArray(json);
		List<SongCi> songCiList = array.toList(SongCi.class);
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}
}
