package org.serendipity.restapi.assembler;

import org.serendipity.restapi.controller.IndividualController;
import org.serendipity.restapi.entity.Individual;
import org.serendipity.restapi.entity.IndividualName;
import org.serendipity.restapi.model.IndividualModel;
import org.serendipity.restapi.model.IndividualNameModel;
import org.serendipity.restapi.type.au.IndividualNameType;
import org.serendipity.restapi.type.au.Sex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
// @Slf4j
public class IndividualModelAssembler extends RepresentationModelAssemblerSupport<Individual, IndividualModel> {

  @Autowired
  private PartyModelAssembler partyModelAssembler;

  public IndividualModelAssembler() {
    super(IndividualController.class, IndividualModel.class);
  }

  @Override
  public IndividualModel toModel(Individual entity) {

    IndividualModel model = instantiateModel(entity);

    model.setId(entity.getId());
    model.setParty(partyModelAssembler.toModel(entity.getParty()));

    model.setTitle(entity.getName().getTitle());
    model.setGivenName(entity.getName().getGivenName());
    model.setMiddleName(entity.getName().getMiddleName());
    model.setFamilyName(entity.getName().getFamilyName());
    model.setHonorific(entity.getName().getHonorific());
    model.setSalutation(entity.getName().getSalutation());
    model.setPreferredName(entity.getName().getPreferredName());
    model.setInitials(entity.getName().getInitials());

    model.setNames(toIndividualNameModel(entity.getNames()));
    model.setSex(entity.getSex());
    model.setEmail(entity.getEmail());
    model.setPhoneNumber(entity.getPhoneNumber());

    model.setPhotoUrl("male-avatar.svg");

    if (entity.getSex().equals(Sex.FEMALE.toString())) {
      model.setPhotoUrl("female-avatar.svg");
    }

    // "photos/albanese-anthony.jpg"
    String url = "photos/" + entity.getName().getFamilyName().toLowerCase() + "-" +
      entity.getName().getGivenName().toLowerCase() + ".jpg";

    if (new ClassPathResource("public/" + url).exists()) {
      model.setPhotoUrl(url);
    }

    // log.info("photoUrl: {}", model.getPhotoUrl());

    model.setElectorate(entity.getElectorate());
    model.setDateOfBirth(entity.getDateOfBirth());
    model.setPlaceOfBirth(entity.getPlaceOfBirth());

    return model;
  }

  private Set<IndividualNameModel> toIndividualNameModel(Set<IndividualName> names) {

    if (names.isEmpty()) {
      return Collections.emptySet();
    }

    return names.stream()
      .map(individualName -> IndividualNameModel.builder()
        .id(individualName.getId())
        .type(individualName.getType())
        .title(individualName.getName().getTitle())
        .givenName(individualName.getName().getGivenName())
        .middleName(individualName.getName().getMiddleName())
        .familyName(individualName.getName().getFamilyName())
        .honorific(individualName.getName().getHonorific())
        .salutation(individualName.getName().getSalutation())
        .preferredName(individualName.getName().getPreferredName())
        .initials(individualName.getName().getInitials())
        .fromDate(individualName.getFromDate())
        .toDate(individualName.getToDate())
        .build())
      .collect(Collectors.toSet());

  }

}

/*

public class IndividualModelAssembler extends RepresentationModelAssemblerSupport<Individual, IndividualModel> {

  @Autowired
  private PartyModelAssembler partyModelAssembler;

  public IndividualModelAssembler() {
    super(IndividualController.class, IndividualModel.class);
  }

  @Override
  public IndividualModel toModel(Individual entity) {

    IndividualModel model = instantiateModel(entity);

    model.setId(entity.getId());
    model.setParty(partyModelAssembler.toModel(entity.getParty()));

    model.setNames(toIndividualNameModel(entity.getNames()));
    model.setSex(entity.getSex());
    model.setEmail(entity.getEmail());
    model.setPhoneNumber(entity.getPhoneNumber());

    model.setPhotoUrl("male-avatar.svg");

    if (entity.getSex().equals(Sex.FEMALE.toString())) {
      model.setPhotoUrl("female-avatar.svg");
    }

    for (IndividualName individualName : entity.getNames()) {

      if (individualName.getType().equals(IndividualNameType.LEGAL_NAME.toString())) {

        // "photos/albanese-anthony.jpg"
        String url = "photos/" + individualName.getFamilyName().toLowerCase() + "-" +
          individualName.getGivenName().toLowerCase() + ".jpg";

        if (new ClassPathResource("public/" + url).exists()) {
          model.setPhotoUrl(url);
        }

        break;
      }

    }

    // log.info("photoUrl: {}", model.getPhotoUrl());

    model.setElectorate(entity.getElectorate());
    model.setDateOfBirth(entity.getDateOfBirth());
    model.setPlaceOfBirth(entity.getPlaceOfBirth());

    return model;
  }

  private Set<IndividualNameModel> toIndividualNameModel(Set<IndividualName> names) {

    if (names.isEmpty()) {
      return Collections.emptySet();
    }

    return names.stream()
      .map(name -> IndividualNameModel.builder()
        .id(name.getId())
        .type(name.getType())
        .title(name.getTitle())
        .givenName(name.getGivenName())
        .middleName(name.getMiddleName())
        .familyName(name.getFamilyName())
        .honorific(name.getHonorific())
        .salutation(name.getSalutation())
        .preferredName(name.getPreferredName())
        .initials(name.getInitials())
        .fromDate(name.getFromDate())
        .toDate(name.getToDate())
        .build())
      .collect(Collectors.toSet());

  }

}

*/
