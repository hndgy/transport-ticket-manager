CREATE TABLE `utilisateur`
(
    `id`     int unsigned NOT NULL auto_increment,
    `nom`    varchar(255) default NULL,
    `prenom` varchar(255) default NULL,
    `mail`   varchar(255) default NULL,
    `mdp`    varchar(255),
    PRIMARY KEY (`id`)
) AUTO_INCREMENT = 1;



CREATE TABLE `tarifs`
(
    `id`           int unsigned NOT NULL auto_increment,
    `type_produit` varchar(255)  default NULL,
    `prix`         decimal(5, 2) default 0,
    `actif`        boolean       default false,
    PRIMARY KEY (`id`)
) AUTO_INCREMENT = 1;


CREATE TABLE `abonnement`
(
    `id`         int unsigned auto_increment,
    `date_debut` datetime     null,
    `date_fin`   datetime     null,
    `id_tarif`   int unsigned null,
    `id_user`    int unsigned null,
    constraint `abonnement_pk`
        primary key (`id`),
    constraint `abonnement_tarif_fk`
        foreign key (`id_tarif`) references `tarifs` (`id`) on update cascade on delete set null,
    constraint `abonnement_utilisateur_fk`
        foreign key (`id_user`) references `utilisateur` (`id`) on update cascade on delete set null
) auto_increment = 1;


create table `ticket`
(
    `id`       int unsigned auto_increment,
    `id_tarif` int unsigned null,
    `nbVoyage` int          null,
    `id_user`  int unsigned null,
    constraint `ticket_pk`
        primary key (`id`),
    constraint `ticket_tarifs_fk`
        foreign key (`id_tarif`) references `tarifs` (`id`) on update cascade on delete set null,
    constraint `ticket_user_fk`
        foreign key (`id_user`) references `utilisateur` (`id`) on update cascade on delete set null
);




