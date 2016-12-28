package com.gdwii.sample.namecheck;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
/**
 * 使用注解处理器API来编写一款拥有自己编码风格的校验工具
 */
@SupportedAnnotationTypes("*") // 表示支持所有的Annotations
@SupportedSourceVersion(SourceVersion.RELEASE_8) // 只支持jdk 1.8的Java代码
public class NameCheckProcessor extends AbstractProcessor{
	private NameChecker nameChecker;
	
	/**
	 * 初始化名称检查插件
	 */
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		nameChecker = new NameChecker(processingEnv);
	}
	
	/**
	 * 对输入的语法树的各个节点进行名称检查
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if(!roundEnv.processingOver()){
			for(Element element : roundEnv.getRootElements()){
				nameChecker.checkNames(element);
			}
		}
		return false;
	}
}
