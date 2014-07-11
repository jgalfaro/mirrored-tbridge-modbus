#include <stdio.h>
#include <cstdio>
#include <jni.h>

#ifdef _WIN32
#define PATH_SEPARATOR ';'
#include <tchar.h>
#else
#define PATH_SEPARATOR ':'
#endif

int main() {
	JavaVM *jvm;       /* denotes a Java VM */
	JNIEnv *env;       /* pointer to native method interface */
	JavaVMInitArgs vm_args; /* JDK/JRE 6 VM initialization arguments */
	JavaVMOption* options = new JavaVMOption[1];
	char classpath[100];
	#ifdef _WIN32
	sprintf_s(classpath, "%s%c%c%s%c%s", "-Djava.class.path=", '.', PATH_SEPARATOR, ".\\wimpi.jar",
		PATH_SEPARATOR, ".\\libs\\wimpi.jar");
	#else
	sprintf(classpath, "%s%c%c%s%c%s", "-Djava.class.path=", '.', PATH_SEPARATOR, "./wimpi.jar",
		PATH_SEPARATOR, "./libs/wimpi.jar");
	#endif
	options[0].optionString = classpath;

	vm_args.version = JNI_VERSION_1_6;
	vm_args.nOptions = 1;
	vm_args.options = options;
	vm_args.ignoreUnrecognized = false;
	/* load and initialize a Java VM, return a JNI interface pointer in env */
	long status = JNI_CreateJavaVM(&jvm, (void **)&env, &vm_args);
	delete options;

	if (status != JNI_ERR){
		jclass clToll = env->FindClass("Main");
		if (clToll != NULL){
			jmethodID main = env->GetStaticMethodID(clToll, "main", "([Ljava/lang/String;)V");
			env->CallStaticVoidMethod(clToll, main, NULL);
		}
		else{
			printf("Error Finding class\n");
		}

		jvm->DestroyJavaVM();
	}else{
		printf("Cannot initialize Java Virtual Machine");
	}


	return 0;
}

