package org.openxdata.xform

import org.openxdata.server.admin.model.FormDef
import org.openxdata.server.admin.model.FormDefVersion
import org.openxdata.server.admin.model.StudyDef


class StudyImporter {

	def xml
	def study = new StudyDef()
	
	def StudyImporter(def xml) {
		this.xml = xml
	}

	def extractStudy() {

		setStudyName()
		setStudyDescription()
		setStudyKey()

		setStudyFormsAndPotentiallyFormVersions()

		return study
	}

	private setStudyName(){
		def studyName = xml.@name.text()
		study.setName(studyName)
	}

	private setStudyDescription(){
		def studyDescription = xml.@description.text()
		study.setDescription(studyDescription)
	}

	private setStudyKey(){
		def studyKey = xml.@studyKey.text()
		study.setStudyKey(studyKey)
	}

	private setStudyFormsAndPotentiallyFormVersions(){
		def forms = []

		xml.form.each {
			def form = createForm(it)
			forms.add(form)
		}

		study.setForms(forms)
	}

	private createForm(def formNode) {
		def form = new FormDef()

		def formName = formNode.@name.text()
		form.setName(formName)

		def formDescription = formNode.@description.text()
		form.setDescription(formDescription)
		
		def formVersions = createFormVersions(formNode)
		
		form.setVersions(formVersions)

		return form
	}
	
	private createFormVersions(def formNode){
		def versions = []
		formNode.version.each {
			def version = extractFormVersion(it)
			versions.add(version)
		}
		
		return versions
	}
	
	private extractFormVersion(def versionNode){
		def version = new FormDefVersion()
		
		def versionName = versionNode.@name.text()
		version.setName(versionName)
		
		def versionDescription = versionNode.@description.text()
		version.setDescription(versionDescription)
		
		def xform = versionNode.xform[0].text()
		version.setXform(xform)
		
		return version
	}
}
