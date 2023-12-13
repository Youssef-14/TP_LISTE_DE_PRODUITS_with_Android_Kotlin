package com.example.todolistkotlin.models

class Product {
    var id: Int? = null
    var Libelle: String? = null
    var PrixVente: Double? = null
    var Codebarre: String? = null
    var Photo: ByteArray? = null

    constructor(Id:Int?, Libelle: String?, PrixVente: Double?, Codebarre: String? , Photo: ByteArray?) {
        this.id = Id
        this.Libelle = Libelle
        this.PrixVente = PrixVente
        this.Codebarre = Codebarre
        this.Photo = Photo
    }

    override fun toString(): String {
        return  Libelle + '\n' +
                PrixVente + '\n' +
                Codebarre + '\n' +
                Photo
    }
}