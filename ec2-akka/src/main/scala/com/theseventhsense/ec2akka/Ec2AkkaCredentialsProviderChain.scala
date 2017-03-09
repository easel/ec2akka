package com.theseventhsense.ec2akka

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth.{AWSCredentialsProviderChain, EnvironmentVariableCredentialsProvider, InstanceProfileCredentialsProvider, SystemPropertiesCredentialsProvider}

/**
  * Created by erik on 5/20/16.
  */
class Ec2AkkaCredentialsProviderChain
    extends AWSCredentialsProviderChain(
        new EnvironmentVariableCredentialsProvider,
        new SystemPropertiesCredentialsProvider,
        new ProfileCredentialsProvider,
        new InstanceProfileCredentialsProvider
    )
