'use client'

import React from 'react'
import { useAuthGuard } from '@/lib/auth/use-auth'
import { Separator } from '@/components/ui/separator'
import Loading from '@/components/loading'
import Container from '@/components/container'

export default function ProfilePage() {
  const { user } = useAuthGuard({ middleware: 'auth' })

  if (!user) return <Loading />

  return (
    <Container size="sm">
      <div className="flex flex-col gap-y-4">
        <h1 className="text-2xl font-semibold">
          Welcome back, {user.firstName}
        </h1>
      </div>
    </Container>
  )
}
