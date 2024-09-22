package io.github.dumijdev.jambaui.context.scanner;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationScanner {

    // Map para armazenar as classes anotadas por uma determinada anotação
    private final Map<Class<? extends Annotation>, Set<Class<?>>> annotatedClasses = new HashMap<>();
    private final Map<Class<? extends Annotation>, Set<Class<?>>> annotatedInterfaces = new HashMap<>();

    // Map para armazenar as interfaces e as classes que as implementam
    private final Map<Class<?>, Set<Class<?>>> interfaceImplementations = new HashMap<>();

    // Map para armazenar as subclasses de uma classe base
    private final Map<Class<?>, Set<Class<?>>> subClasses = new HashMap<>();

    // Logger
    private final Logger logger = LoggerFactory.getLogger(ApplicationScanner.class);

    // Singleton
    private static final ApplicationScanner instance = new ApplicationScanner();

    private ApplicationScanner() {
    }

    public static ApplicationScanner getInstance() {
        return instance;
    }

    /**
     * Escaneia os pacotes fornecidos e organiza os resultados.
     *
     * @param basePackages Pacotes a serem escaneados.
     */
    public void scanPackages(String... basePackages) {
        try (var result = new ClassGraph().enableAllInfo().acceptPackages(basePackages).scan()) {
            logger.debug("Scaning packages, {}", Arrays.toString(basePackages));
            processScanResult(result);
            logger.debug("Packages scan finished.");
        } catch (Exception e) {
            logger.error("Falha ao escanear pacotes: {}", Arrays.toString(basePackages), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Processa o resultado do escaneamento e organiza as classes encontradas.
     */
    private void processScanResult(ScanResult result) {

        result.getAllClasses().forEach(classInfo -> {
            Class<?> clazz = null;
            try {
                clazz = classInfo.loadClass();
            } catch (Exception e) {
            }

            if (clazz == null) {
                return;
            }

            for (var annotation : clazz.getAnnotations()) {
                annotatedClasses
                        .computeIfAbsent(annotation.annotationType(), k -> new HashSet<>())
                        .add(clazz);
            }
            // Processar implementações de interfaces
            for (var iface : clazz.getInterfaces()) {
                interfaceImplementations
                        .computeIfAbsent(iface, k -> new HashSet<>())
                        .add(clazz);
            }

            // Processar subclasses
            if (clazz.getSuperclass() != null) {
                subClasses
                        .computeIfAbsent(clazz.getSuperclass(), k -> new HashSet<>())
                        .add(clazz);
            }

            if (clazz.isInterface()) {
                for (var annotation : clazz.getAnnotations()) {
                    annotatedInterfaces
                            .computeIfAbsent(annotation.annotationType(), k -> new HashSet<>())
                            .add(clazz);
                }
            }
        });
    }

    // MÉTODOS PARA CONSULTAR OS DADOS ESCANEADOS

    /**
     * Retorna todas as classes anotadas por uma anotação específica.
     *
     * @param annotationClass Classe da anotação.
     * @return Set de classes anotadas pela anotação fornecida.
     */
    public Set<Class<?>> getClassesAnnotatedBy(Class<? extends Annotation> annotationClass) {
        logger.debug("Retrieving classes annotated by {}", annotationClass.getSimpleName());
        return annotatedClasses.getOrDefault(annotationClass, Collections.emptySet())
                .stream()
                .filter(this::isConcreteClasses)
                .collect(Collectors.toSet());
    }

    public Set<Class<?>> getAnnotationsAnnotatedWith(Class<? extends Annotation> annotationClass) {
        return annotatedClasses.getOrDefault(annotationClass, Collections.emptySet())
                .stream()
                .filter(Class::isAnnotation)
                .collect(Collectors.toSet());
    }

    public Set<Class<?>> getInterfacesAnnotatedBy(Class<? extends Annotation> annotationClass) {
        return annotatedInterfaces.getOrDefault(annotationClass, Collections.emptySet());
    }

    private boolean isConcreteClasses(Class<?> clazz) {
        return !(clazz.isAnnotation() || clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()));
    }

    /**
     * Retorna todas as classes que implementam uma interface específica.
     *
     * @param interfaceClass Classe da interface.
     * @return Set de classes que implementam a interface fornecida.
     */
    public Set<Class<?>> getClassesImplementingInterface(Class<?> interfaceClass) {
        return interfaceImplementations.getOrDefault(interfaceClass, Collections.emptySet());
    }

    /**
     * Retorna todas as subclasses de uma classe base específica.
     *
     * @param baseClass Classe base.
     * @return Set de classes que são subclasses da classe base fornecida.
     */
    public Set<Class<?>> getSubClassesOf(Class<?> baseClass) {
        return subClasses.getOrDefault(baseClass, Collections.emptySet());
    }

    /**
     * Retorna todas as classes anotadas no sistema.
     *
     * @return Map com as classes anotadas organizadas por anotação.
     */
    public Map<Class<? extends Annotation>, Set<Class<?>>> getAllAnnotatedClasses() {
        return Collections.unmodifiableMap(annotatedClasses);
    }

    /**
     * Retorna todas as implementações de interfaces no sistema.
     *
     * @return Map com as interfaces e suas implementações.
     */
    public Map<Class<?>, Set<Class<?>>> getAllInterfaceImplementations() {
        return Collections.unmodifiableMap(interfaceImplementations);
    }

    /**
     * Retorna todas as subclasses registradas no sistema.
     *
     * @return Map com as classes base e suas subclasses.
     */
    public Map<Class<?>, Set<Class<?>>> getAllSubClasses() {
        return Collections.unmodifiableMap(subClasses);
    }

}
