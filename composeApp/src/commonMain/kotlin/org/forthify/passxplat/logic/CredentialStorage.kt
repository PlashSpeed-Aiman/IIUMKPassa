package org.forthify.passxplat.logic

import org.forthify.passxplat.model.StudentCredentials

interface CredentialStorage{
     fun load (): StudentCredentials
     fun save (credentials: StudentCredentials) : Unit
 }
