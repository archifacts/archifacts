package org.archifacts.example.jmolecules;

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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.ViewSet;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import org.archifacts.core.model.Application;
import org.archifacts.core.model.ArtifactContainer;
import org.archifacts.core.model.BuildingBlock;
import org.archifacts.integration.asciidoc.AsciiDoc;
import org.archifacts.integration.c4.asciidoc.plantuml.ComponentViewPlantUMLDocElement;
import org.archifacts.integration.c4.asciidoc.plantuml.ContainerViewPlantUMLDocElement;
import org.archifacts.integration.c4.model.C4ModelTransformer;
import org.archifacts.integration.plaintext.ApplicationOverview;

public class JMoleculesDocumenter {

	public static void main(final String[] args) throws IOException {
		new JMoleculesDocumenter().generateDocumentation();

	}

	private static final String ApplicationPackage = "org.jmolecules.examples.jpa";
	private static final ModuleDescriptor ModuleDescriptor = new ModuleDescriptor(ApplicationPackage);

	private void generateDocumentation() throws IOException {
		final JavaClasses javaClasses = new ClassFileImporter().importPackages(ApplicationPackage);
		final Application application = initApplication(javaClasses);
		writeApplicationOverviewToFile(application, Paths.get("export", "jmolecules-spring-data-jpa-example.txt"));
		writeC4ModelToFile(application, Paths.get("export", "jmolecules-spring-data-jpa-example.adoc"));
	}

	private void writeApplicationOverviewToFile(final Application application, Path outputFile) throws IOException {
		Files.createDirectories(outputFile.getParent());
		try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
			new ApplicationOverview(application).writeToWriter(writer);
		}
		System.out.println("Application overview written to " + outputFile.toString());
	}

	private void writeC4ModelToFile(final Application application, Path outputFile) throws IOException {
		final Workspace c4Workspace = new Workspace("jMolecules - Spring Data JPA Example", null);
		final ViewSet views = c4Workspace.getViews();
		final SoftwareSystem softwareSystem = initSoftwareSystem(c4Workspace);
		final C4ModelTransformer c4ModelTransformer = new C4ModelTransformer(application, softwareSystem);

		final AsciiDoc asciiDoc = new AsciiDoc();

		final ContainerView containerView = initContainerView(softwareSystem, views, c4ModelTransformer);

		asciiDoc.addDocElement(new ContainerViewPlantUMLDocElement(containerView));

		c4ModelTransformer.getContainers(this::isModule)
				.stream()
				.map(module -> initComponentView(module, views, c4ModelTransformer))
				.map(ComponentViewPlantUMLDocElement::new)
				.forEach(asciiDoc::addDocElement);
		Files.createDirectories(outputFile.getParent());
		try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
			asciiDoc.writeToWriter(writer);
		}
		System.out.println("C4 model written to " + outputFile.toString());
	}

	private boolean isModule(final ArtifactContainer container) {
		return container.getType().equals(ModuleDescriptor.type());
	}

	private Application initApplication(final JavaClasses javaClasses) {
		return Application.builder()
				.addContainerDescriptor(ModuleDescriptor)
				.addContainerDescriptor(JdkLibraryDescriptor.INSTANCE)
				.addBuildingBlockDescriptor(AggregateRootDescriptor)
				.addBuildingBlockDescriptor(EntityDescriptor)
				.addBuildingBlockDescriptor(IdentifierDescriptor)
				.addBuildingBlockDescriptor(EventDescriptor)
				.addBuildingBlockDescriptor(ServiceDescriptor)
				.addBuildingBlockDescriptor(RepositoryDescriptor)
				.addSourceBasedRelationshipDescriptor(EventHandlerDescriptor)
				.addSourceBasedRelationshipDescriptor(IdentifiedByDescriptor)
				.addSourceBasedRelationshipDescriptor(ManagedByDescriptor)
				.addSourceBasedRelationshipDescriptor(AggregateRootAssociationDescriptor)
				.addSourceBasedRelationshipDescriptor(ContainedEntityDescriptor)
				.buildApplication(javaClasses);

	}

	private SoftwareSystem initSoftwareSystem(final Workspace workspace) {
		final Model model = workspace.getModel();
		model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy());
		final SoftwareSystem softwareSystem = model.addSoftwareSystem("jMolecules - Spring Data JPA Example");
		return softwareSystem;
	}

	private ContainerView initContainerView(final SoftwareSystem softwareSystem, final ViewSet views, final C4ModelTransformer c4ModelTransformer) {

		final ContainerView containerView = views.createContainerView(softwareSystem, "container-view", "Module overview");
		containerView.addAllContainers();
		containerView.enableAutomaticLayout();
		c4ModelTransformer.getContainers(not(this::isModule)).forEach(containerView::remove);
		c4ModelTransformer.getNoContainerContainer().ifPresent(containerView::remove);
		return containerView;
	}

	private ComponentView initComponentView(final Container container, final ViewSet views, final C4ModelTransformer c4ModelTransformer) {

		final ComponentView componentView = views.createComponentView(container, container.getId(), null);
		componentView.addAllComponents();
		componentView.addExternalDependencies();
		c4ModelTransformer.getComponents(not(BuildingBlock.class::isInstance))
				.stream()
				.filter(component -> component.getRelationships().isEmpty())
				.forEach(componentView::remove);
		c4ModelTransformer.getNoContainerContainer().ifPresent(componentView::remove);
		return componentView;
	}
}
