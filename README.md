# testpb
protocol buffer Android demo

## Google Protocol Buffer 介绍与使用

### 简介

- 官网[https://developers.google.com/protocol-buffers/](https://developers.google.com/protocol-buffers/)

- Google Protocol Buffer( 以下简称PB) 是 Google 公司内部的混合语言数据标准，目前已经正在使用的有超过 48,162 种报文格式定义和超过 12,183 个 .proto 文件。他们用于 RPC 系统和持续数据存储系统。PB是一种轻便高效的结构化数据存储格式，可以用于结构化数据串行化，或者说序列化。它很适合做数据存储或 RPC 数据交换格式。可用于通讯协议、数据存储等领域的语言无关、平台无关、可扩展的序列化结构数据格式。

 > “Protocol Buffers是一种以有效并可扩展的格式编码结构化数据的方式。”

	>     message Person {
	>       required int32 id = 1;
	>       required string name = 2;
	>       optional string email = 3;
	>     }

### PB 的优点
  
- 性能好，效率高

> 同 XML 相比， PB 的主要优点在于性能高。它以高效的二进制方式存储，比 XML 小 3 到 10 倍，快 20 到 100 倍。Protobuf 序列化后所生成的二进制消息非常紧凑，这得益于 Protobuf 采用的非常巧妙的 [Encoding](https://developers.google.com/protocol-buffers/docs/encoding) 方法。
> 
> Total Time 指一个对象操作的整个时间，包括创建对象，将对象序列化为内存中的字节序列，然后再反序列化的整个过程。从测试结果可以看到 Protobuf 的成绩很好，感兴趣的读者可以自行到[网站]( http://code.google.com/p/thrift-protobuf-compare/wiki/Benchmarking)上了解更详细的测试结果。


- 代码生成机制，数据解析类自动生成

> PB 语义更清晰，无需类似 XML 解析器的东西（因为 PB 编译器会将 .proto 文件编译生成对应的数据访问类以对 PB数据进行序列化、反序列化操作）。使用 PB无需学习复杂的文档对象模型，PB 的编程模式比较友好，简单易学，同时它拥有良好的文档和示例，对于喜欢简单事物的人们而言，PB 比其他的技术更加有吸引力

- 支持向后兼容和向前兼容

> 它有一个非常棒的特性，即“向后”兼容性好，人们不必破坏已部署的、依靠“老”数据格式的程序就可以对数据结构进行升级。这样您的程序就可以不必担心因为消息结构的改变而造成的大规模的代码重构或者迁移的问题。因为添加新的消息中的 field 并不会引起已经发布的程序的任何改变。

### PB 使用(java)
- step1： 下载protobuf

> [下载](https://github.com/google/protobuf)并编译获得proto.exe和protobuf-java.jar
> 也可以直接从网上下载proto.exe [下载](http://repo1.maven.org/maven2/com/google/protobuf/protoc/)和protobuf-java-2.4.1.jar（[下载](https://github.com/google/protobuf/releases)）

- step2：编写.proto文件，eg：

``` C

    option java_package = "com.jeson.roster.proto";  
    option java_outer_classname = "Roster";  
      
    message Student{  
    required int32 id = 1;  
    required string name = 2;   
      
    enum Sex{  
    MALE = 0;  
    FEMALE = 1;  
    }  
      
    required Sex sex = 3;  
      
    }  
     
    message StudentRoster{  
    repeated Student student = 1;  
    }  

```

- step3：编译.proto文件生成 java类文件：在proto.exe目录下：protoc  --java_out=./src   ./proto/msg.proto

> 在所使用的proto文件路径下打开cmd窗口执行以下命令

``` C

    protoc -I=源地址 --java_out=目标地址  源地址/xxx.proto
```

- step4：导入java类文件和依赖包直接使用

> Android Studio eg :   [完整demo地址](https://github.com/bingxuebage/testpb)
> 
> > 1 添加依赖包 compile 'com.google.protobuf:protobuf-java:3.0.0-beta-2'
> > 
> > 2 导入生成的 Roster 到对应的package下
> >
> > 3 使用Roster 序列化或反序列化数据


``` java

	package com.jeson.testpb;
	
	import com.jeson.roster.proto.Roster.Student;
	import com.jeson.roster.proto.Roster.Student.Sex;
	import com.jeson.roster.proto.Roster.StudentRoster;
	
	import java.io.FileInputStream;
	import java.io.FileNotFoundException;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.List;
	
	public class InfoHelper {
	
	
	    public static final String FILE_NAME = "/mnt/sdcard/roster.dat";
	
	
	    /**
	     * 反序列化
	     */
	
	    public static List<StudentInfo> getStudentsFromFile() {
	
	        ArrayList<StudentInfo> list = new ArrayList<StudentInfo>();
	        FileInputStream fis = null;
	
	        try {
	            fis = new FileInputStream(FILE_NAME);
	            StudentRoster roster = StudentRoster.parseFrom(fis);
	            int student_count = roster.getStudentCount();
	
	            for (int i = 0; i < student_count; i++) {
	                Student student = roster.getStudent(i);
	                StudentInfo info = new StudentInfo();
	                info.setId(student.getId());
	                info.setName(student.getName());
	                info.setSex(student.getSex().toString());
	                list.add(info);
	            }
	
	        } catch (FileNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } finally {
	            if (null != fis) {
	                try {
	                    fis.close();
	                } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        }
	
	        return list;
	    }
	
	
	    /**
	     * 序列化
	     */
	
	    public static void saveStudentIntoFile(int id, String name, String sex) {
	        FileOutputStream fos = null;
	
	        StudentRoster.Builder rosterBuid = StudentRoster.newBuilder();
	
	
	        Student.Builder student = Student.newBuilder();
	        student.setId(id);
	        student.setName(name);
	        if ("MALE".equalsIgnoreCase(sex)) {
	            student.setSex(Sex.MALE);
	        } else {
	            student.setSex(Sex.FEMALE);
	        }
	
	        rosterBuid.addStudent(student.build());
	
	        try {
	            fos = new FileOutputStream(FILE_NAME, true);
	            rosterBuid.build().writeTo(fos);
	            fos.close();
	        } catch (FileNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	
	
	    }
	
	}


```   
