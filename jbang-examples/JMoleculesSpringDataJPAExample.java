///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 11+
//DEPS org.archifacts:archifacts-core:0.2.0
//DEPS org.archifacts:archifacts-jmolecules:0.2.0
//DEPS org.archifacts:archifacts-c4-asciidoc:0.2.0
//DEPS org.asciidoctor:asciidoctorj:2.5.2
//DEPS org.asciidoctor:asciidoctorj-diagram:2.2.1
//DEPS org.eclipse.jetty:jetty-server:11.0.6

import static java.util.function.Predicate.not;
import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.BuildingBlockDescriptors.AggregateRootDescriptor;
import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.BuildingBlockDescriptors.EntityDescriptor;
import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.BuildingBlockDescriptors.EventDescriptor;
import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.BuildingBlockDescriptors.IdentifierDescriptor;
import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.BuildingBlockDescriptors.RepositoryDescriptor;
import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.BuildingBlockDescriptors.ServiceDescriptor;
import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.RelationshipDescriptors.AggregateRootAssociationDescriptor;
import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.RelationshipDescriptors.ContainedEntityDescriptor;
import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.RelationshipDescriptors.EventHandlerDescriptor;
import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.RelationshipDescriptors.IdentifiedByDescriptor;
import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.RelationshipDescriptors.ManagedByDescriptor;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.archifacts.core.descriptor.ArtifactContainerDescriptor;
import org.archifacts.core.model.Application;
import org.archifacts.core.model.ArtifactContainer;
import org.archifacts.core.model.ArtifactContainerType;
import org.archifacts.core.model.BuildingBlock;
import org.archifacts.core.model.BuildingBlockType;
import org.archifacts.integration.asciidoc.AsciiDoc;
import org.archifacts.integration.asciidoc.TableDocElement;
import org.archifacts.integration.c4.asciidoc.plantuml.ComponentViewPlantUMLDocElement;
import org.archifacts.integration.c4.asciidoc.plantuml.ContainerViewPlantUMLDocElement;
import org.archifacts.integration.c4.model.C4ModelTransformer;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.ViewSet;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaPackage;
import com.tngtech.archunit.core.importer.ClassFileImporter;

public class JMoleculesSpringDataJPAExample {

	private static final NumberFormat numberFormatter = NumberFormat.getInstance();
	private static final int SERVER_PORT = 8080;
	
	public static void main(final String[] args) throws Exception {
		new JMoleculesSpringDataJPAExample().generateDocumentation();

	}

	private static final String ApplicationPackage = "org.jmolecules.examples.jpa";
	private static final ModuleDescriptor ModuleDescriptor = new ModuleDescriptor(ApplicationPackage);

	private JarFile jMoleculesExampleJar() throws IOException {
		Path tempFile = Files.createTempFile("", ".jar");
		try (InputStream in = new URL(
				"https://repo1.maven.org/maven2/org/jmolecules/integrations/jmolecules-spring-data-jpa/0.3.0/jmolecules-spring-data-jpa-0.3.0.jar")
						.openStream()) {
			Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
		}
		return new JarFile(tempFile.toFile());
	}

	private void generateDocumentation() throws Exception  {
		JarFile jMoleculesExampleJar = runWithResult("Downloading jMolecules example", this::jMoleculesExampleJar);
		final JavaClasses javaClasses = runWithResult("Reading the application", () -> new ClassFileImporter().importJar(jMoleculesExampleJar));
		final Application application = runWithResult("Initializing the archifacts model", () -> initApplication(javaClasses));
		StringWriter writer = new StringWriter();
		run("Generating the AsciiDoc documentation", () -> {
			writeC4ModelToFile(application, writer);
			writeBuildingBlocksTableToFile(application, writer);
		});
		Path jMoleculesDirectory = Files.createTempDirectory("jmolecules");
		final Path htmlFile = jMoleculesDirectory.resolve("jmolecules.html");
		run("Transforming to html", () -> {
			try (Asciidoctor asciidoctor = Asciidoctor.Factory.create()) {
				asciidoctor.requireLibrary("asciidoctor-diagram");
				asciidoctor.convert(writer.toString(), 
						Options.builder()
						.backend("html5")
						.toFile(htmlFile.toFile())
						.mkDirs(true)
						.safe(SafeMode.UNSAFE)
						.build());
			}
			 
		});
		
		serveDocumentation(htmlFile);
		openInBrowser("http://localhost:8080");
		System.out.println("Press enter to stop...");
		System.in.read();
		System.exit(0);
	}
	
	private void serveDocumentation(Path htmlFile) throws Exception {
		run("Starting web server at port " + SERVER_PORT, () -> {
			Server server = new Server(8080);
			
			ResourceHandler resourceHandler = new ResourceHandler();
			resourceHandler.setWelcomeFiles(new String[] {htmlFile.getFileName().toString()});
			resourceHandler.setDirAllowed(true);
			resourceHandler.setResourceBase(htmlFile.getParent().toAbsolutePath().toString());
			server.setHandler(resourceHandler);
			
			server.start();
		});
	}

	private void writeC4ModelToFile(final Application application, Writer writer) throws IOException {
		final Workspace c4Workspace = new Workspace("jMolecules - Spring Data JPA Example", null);
		final ViewSet views = c4Workspace.getViews();
		final SoftwareSystem softwareSystem = initSoftwareSystem(c4Workspace);
		final C4ModelTransformer c4ModelTransformer = new C4ModelTransformer(application, softwareSystem);

		final AsciiDoc asciiDoc = new AsciiDoc();

		final ContainerView containerView = initContainerView(softwareSystem, views, c4ModelTransformer);

		asciiDoc.addDocElement(new ContainerViewPlantUMLDocElement(containerView));

		c4ModelTransformer.getContainers(this::isModule).stream()
				.map(module -> initComponentView(module, views, c4ModelTransformer))
				.map(ComponentViewPlantUMLDocElement::new).forEach(asciiDoc::addDocElement);
		asciiDoc.writeToWriter(writer);
	}

	private boolean isModule(final ArtifactContainer container) {
		return container.getType().equals(ModuleDescriptor.type());
	}

	private Application initApplication(final JavaClasses javaClasses) {
		return Application.builder().addContainerDescriptor(ModuleDescriptor)
				.addContainerDescriptor(JdkLibraryDescriptor.INSTANCE)
				.addBuildingBlockDescriptor(AggregateRootDescriptor).addBuildingBlockDescriptor(EntityDescriptor)
				.addBuildingBlockDescriptor(IdentifierDescriptor).addBuildingBlockDescriptor(EventDescriptor)
				.addBuildingBlockDescriptor(ServiceDescriptor).addBuildingBlockDescriptor(RepositoryDescriptor)
				.addSourceBasedRelationshipDescriptor(EventHandlerDescriptor)
				.addSourceBasedRelationshipDescriptor(IdentifiedByDescriptor)
				.addSourceBasedRelationshipDescriptor(ManagedByDescriptor)
				.addSourceBasedRelationshipDescriptor(AggregateRootAssociationDescriptor)
				.addSourceBasedRelationshipDescriptor(ContainedEntityDescriptor).buildApplication(javaClasses);

	}

	private SoftwareSystem initSoftwareSystem(final Workspace workspace) {
		final Model model = workspace.getModel();
		model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy());
		final SoftwareSystem softwareSystem = model.addSoftwareSystem("jMolecules - Spring Data JPA Example");
		return softwareSystem;
	}

	private ContainerView initContainerView(final SoftwareSystem softwareSystem, final ViewSet views,
			final C4ModelTransformer c4ModelTransformer) {

		final ContainerView containerView = views.createContainerView(softwareSystem, "container-view",
				"Module overview");
		containerView.addAllContainers();
		containerView.enableAutomaticLayout();
		c4ModelTransformer.getContainers(not(this::isModule)).forEach(containerView::remove);
		c4ModelTransformer.getNoContainerContainer().ifPresent(containerView::remove);
		return containerView;
	}

	private ComponentView initComponentView(final Container container, final ViewSet views,
			final C4ModelTransformer c4ModelTransformer) {

		final ComponentView componentView = views.createComponentView(container, container.getId(), null);
		componentView.addAllComponents();
		componentView.addExternalDependencies();
		c4ModelTransformer.getComponents(not(BuildingBlock.class::isInstance)).stream()
				.filter(component -> component.getRelationships().isEmpty()).forEach(componentView::remove);
		c4ModelTransformer.getNoContainerContainer().ifPresent(componentView::remove);
		return componentView;
	}

	private void writeBuildingBlocksTableToFile(final Application application, final Writer writer) throws IOException {
		final AsciiDoc asciiDoc = new AsciiDoc();

		final Set<BuildingBlockType> sortedBuildingBlockTypes = application.getBuildingBlocks().stream()
				.map(BuildingBlock::getType).distinct().sorted(Comparator.comparing(BuildingBlockType::getName))
				.collect(Collectors.toCollection(LinkedHashSet::new));
		final TableDocElement<BuildingBlockType> tableDocElement = TableDocElement.forElements(sortedBuildingBlockTypes)
				.title("Building Blocks").column("Name", BuildingBlockType::getName)
				.column("Occurrences", b -> Integer.toString(application.getBuildingBlocksOfType(b).size())).build();
		asciiDoc.addDocElement(tableDocElement);

		asciiDoc.writeToWriter(writer);
	}

	private void openInBrowser(String url) throws IOException {
		final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			run("Starting the browser to access " + url, () -> desktop.browse(new URI(url)));
		}
	}
	
	private <T> T runWithResult(String name, ThrowingSupplier<T> task){
		System.out.print(name + "...");
		final Instant start = Instant.now();
		final T result = task.get();
		final Instant finish = Instant.now();
		System.out.println(" done (took: " + numberFormatter.format(Duration.between(start, finish).toMillis()) + " ms)");
		return result;
	
	}
	
	private void run(String name, ThrowingRunnable task){
		runWithResult(name, () -> {
			task.run();
			return null;
		});
	}

	@FunctionalInterface
	interface ThrowingSupplier<U> extends Supplier<U> {

	    @Override
	    default U get() {
	        try {
	            return getThrows();
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	    U getThrows() throws Exception;
	}
	
	@FunctionalInterface
	interface ThrowingRunnable extends Runnable {
		
		@Override
		default void run() {
			try {
				getThrows();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		void getThrows() throws Exception;
	}
	
	private static final class JdkLibraryDescriptor implements ArtifactContainerDescriptor {

		static final ArtifactContainerType TYPE = ArtifactContainerType.of("Library");

		static final JdkLibraryDescriptor INSTANCE = new JdkLibraryDescriptor();

		private JdkLibraryDescriptor() {

		}

		@Override
		public Optional<String> containerNameOf(final JavaClass javaClass) {
			if (javaClass.getPackageName().startsWith("java.")) {
				return Optional.of("jdk");
			} else {
				return Optional.empty();
			}
		}

		@Override
		public ArtifactContainerType type() {
			return TYPE;
		}

	}

	private static final class ModuleDescriptor implements ArtifactContainerDescriptor {

		private static final ArtifactContainerType TYPE = ArtifactContainerType.of("Module");

		private final String basePackage;

		ModuleDescriptor(final String basePackage) {
			this.basePackage = basePackage;
		}

		@Override
		public Optional<String> containerNameOf(final JavaClass javaClass) {
			return moduleOf(javaClass.getPackage());
		}

		private Optional<String> moduleOf(final JavaPackage javaPackage) {
			return toJavaOptional(javaPackage.getParent()).map(parent -> {
				if (basePackage.equals(parent.getName())) {
					return Optional.of(javaPackage.getRelativeName());
				} else {
					return moduleOf(parent);
				}
			}).orElse(Optional.<String>empty());
		}

		private <T> Optional<T> toJavaOptional(final com.tngtech.archunit.base.Optional<T> optional) {
			return optional.map(Optional::of).orElse(Optional.empty());
		}

		@Override
		public ArtifactContainerType type() {
			return TYPE;
		}
	}
}